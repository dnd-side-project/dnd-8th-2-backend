package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.dto.common.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dnd.reetplace.global.exception.GlobalExceptionType.ACCESS_DENIED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 엔드포인트에 대해 접근 권한이 존재하지 않을 때 동작하는 Handler로, 아래와 같은 Response가 반환된다.
     * "errorCode": " 1003
     * "errorMessage": "접근 권한이 없습니다."
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(ACCESS_DENIED.getHttpStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(),
                new ErrorResponse(ACCESS_DENIED.getErrorCode(), ACCESS_DENIED.getErrorMessage()));
    }
}
