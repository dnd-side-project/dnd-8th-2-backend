package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.auth.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestHeader("access-token") String accessToken) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(accessToken));
    }
}
