package com.dnd.reetplace.app.dto.auth.response;

import lombok.Getter;

import java.util.Map;

@SuppressWarnings("unchecked")
@Getter
public class KakaoProfileResponse {
    private Long id;
    private String connected_at;
    private Map<String, Object> kakao_account;
    private Map<String, Object> properties;

    public String getEmail() {
        return (String) this.getKakao_account().get("email");
    }

    public String getNickname() {
        Map<String, Object> profile = (Map<String, Object>) this.getKakao_account().get("profile");
        return (String) profile.get("nickname");
    }
}
