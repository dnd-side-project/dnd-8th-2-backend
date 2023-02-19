package com.dnd.reetplace.app.dto.auth;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.type.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private Long memberId;

    private String uid;

    private LoginType loginType;

    private String nickname;

    private String accessToken;

    private String refreshToken;

    public static LoginResponse of(
            Member member,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponse(member.getId(), member.getUid(), member.getLoginType(), member.getNickname(), accessToken, refreshToken);
    }
}
