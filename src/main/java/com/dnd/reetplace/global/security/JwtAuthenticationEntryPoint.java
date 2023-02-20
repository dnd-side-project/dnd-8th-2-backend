package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.dto.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dnd.reetplace.global.exception.ExceptionType.AUTHENTICATION_REQUIRED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증이 필요한 엔드포인트에 대해 인증되지 않았을 때 동작하는 Handler로, 아래와 같은 Response가 반환된다.
     * "errorCode": " 1004
     * "errorMessage": "인증이 필요한 요청입니다."
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(),
                new ErrorResponse(AUTHENTICATION_REQUIRED.getCode(), AUTHENTICATION_REQUIRED.getMessage()));
    }
}
