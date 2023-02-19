package com.dnd.reetplace.app.dto.auth;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoProfileResponse {
    private Long id;
    private String connected_at;
    private Map<String, Object> kakao_account;
    private Map<String, Object> properties;
}
