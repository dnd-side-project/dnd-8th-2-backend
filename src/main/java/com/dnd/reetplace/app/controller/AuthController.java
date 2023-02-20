package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.service.OAuth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(request));
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(oAuth2Service.refresh(request));
    }
}
