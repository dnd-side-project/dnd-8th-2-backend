package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.dto.common.ErrorResponse;
import com.dnd.reetplace.global.exception.GlobalExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isLoginPath = antPathMatcher.match("/api/auth/login/**", request.getServletPath());
        if (accessToken != null && !isLoginPath) {
            try {
                tokenProvider.validateToken(accessToken);
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                Class<? extends Exception> type = e.getClass();
                GlobalExceptionType exceptionType = Arrays.stream(GlobalExceptionType.values())
                        .filter(t -> t.getType() != null && t.getType().equals(type))
                        .findFirst()
                        .get();
                response.setStatus(exceptionType.getHttpStatus().value());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(),
                        new ErrorResponse(exceptionType.getErrorCode(), exceptionType.getErrorMessage()));
            }

        }
        filterChain.doFilter(request, response);
    }
}
