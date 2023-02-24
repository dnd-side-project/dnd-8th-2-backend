package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "인증", description = "로그인, 회원가입, 토큰 재발급 등 인증 관련 API입니다.")
public class AuthController {

    private final OAuth2Service oAuth2Service;

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 서버에서 받은 Access Token을 통해 로그인을 진행합니다."
    )
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @Parameter(name = "access-token", description = "카카오 서버에서 받은 Access Token", example = "29WryM8Px6...")
            @RequestHeader("access-token") String token
    ) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(token));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh Token을 통해 Access Token, Refresh Token을 재발급합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @Parameter(name = "Authorization", description = "사용자의 Refresh Token", example = "Bearer eyJ0eXAi...")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(oAuth2Service.refresh(request));
    }
}
