package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.dto.exception.ErrorResponse;
import com.dnd.reetplace.global.exception.ExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    /**
     * 모든 엔드포인트에 대해 동작하여 access token을 확인하는 Filter.
     * Authorization Header에 access token이 존재할 시 이를 검증하고, SecurityContextHolder에 인증 정보를 저장한다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String accessToken = tokenProvider.getToken(request);
        if (accessToken != null) {
            try {
                tokenProvider.validateToken(accessToken);
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                ExceptionType exceptionType = ExceptionType.from(e.getClass()).orElse(ExceptionType.UNHANDLED);
                response.setStatus(UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(),
                        new ErrorResponse(exceptionType.getCode(), exceptionType.getMessage()));
            }

        }
        filterChain.doFilter(request, response);
    }
}
