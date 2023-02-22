package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final OAuth2Service oAuth2Service;

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 서버에서 받은 Access Token을 통해 로그인을 진행합니다.<br>" +
                    "로그인 성공 시, 사용자의 프로필 정보와 Access Token, Refresh Token이 반환됩니다.")
    @Parameter(
            name = "access-token",
            description = "카카오 서버에서 받은 Access Token",
            in = ParameterIn.HEADER,
            example = "29WryM8Px69a4NB8VAX9wzPat9f46Meap24--OoCiolkAAAAYZ3tWXj"
    )
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestHeader("access-token") String token) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(oAuth2Service.refresh(request));
    }
}
