package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.service.OAuth2Service;
import com.dnd.reetplace.app.service.RefreshTokenRedisService;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final RefreshTokenRedisService refreshTokenRedisService;

    @Operation(summary = "카카오 로그인", description = "카카오 서버에서 받은 Access Token을 통해 로그인을 진행합니다.")
    @Parameter(
            name = "access-token", description = "카카오 서버에서 받은 Access Token", in = ParameterIn.HEADER,
            required = true, example = "29WryM8Px6..."
    )
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestHeader("access-token") String token) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(token));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 통해 Access Token, Refresh Token을 재발급합니다.")
    @Parameter(
            name = "Authorization", description = "사용자의 Refresh Token", in = ParameterIn.HEADER,
            required = true, example = "Bearer eyJ0eXAi..."
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(oAuth2Service.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal MemberDetails memberDetails) {
        refreshTokenRedisService.deleteRefreshToken(memberDetails.getUid());
        return ResponseEntity.ok().build();
    }
}
