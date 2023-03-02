package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.dto.survey.request.SurveyRequest;
import com.dnd.reetplace.app.service.OAuth2Service;
import com.dnd.reetplace.app.service.RefreshTokenRedisService;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
@Tag(name = "인증", description = "로그인, 회원가입, 토큰 재발급 등 인증 관련 API입니다.")
public class AuthController {

    private final OAuth2Service oAuth2Service;
    private final RefreshTokenRedisService refreshTokenRedisService;

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
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(oAuth2Service.refresh(request));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그인 된 사용자를 앱에서 로그아웃합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        refreshTokenRedisService.deleteRefreshToken(memberDetails.getUid());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "사용자의 계정을 탈퇴합니다.<br><br>" +
                    "surveyType은 각각 다음과 같습니다.<br>" +
                    "- RECORD_DELETE : 기록 삭제 목적<br>" +
                    "- LOW_USED : 사용 빈도가 낮아서<br>" +
                    "- USE_OTHER_SERVICE : 다른 서비스 사용 목적<br>" +
                    "- INCONVENIENCE_AND_ERRORS : 이용이 불편하고 장애가 많아서<br>" +
                    "- OTHER : 기타 (해당 경우 description field 필수값)",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping("/unlink")
    public ResponseEntity<Void> unlink(
            @Valid @RequestBody SurveyRequest surveyRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(name = "access-token", description = "카카오 서버에서 받은 Access Token", example = "29WryM8Px6...")
            @RequestHeader(value = "access-token") String kakaoAccessToken
    ) {
        oAuth2Service.unlink(memberDetails.getId(), surveyRequest.toDto(), kakaoAccessToken);
        return ResponseEntity.noContent().build();
    }
}
