package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.LoginResponse;
import com.dnd.reetplace.app.service.OAuth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestHeader("access-token") String accessToken) throws JsonProcessingException {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(accessToken));
    }

    @GetMapping("/test")
    public String test() {
        return "test Success!";
    }
}
