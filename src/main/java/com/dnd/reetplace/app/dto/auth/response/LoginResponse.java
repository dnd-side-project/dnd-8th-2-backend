package com.dnd.reetplace.app.dto.auth.response;

import com.dnd.reetplace.app.dto.member.MemberDto;
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
    private String email;
    private String accessToken;
    private String refreshToken;

    public static LoginResponse of(
            MemberDto memberDto,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponse(
                memberDto.getId(),
                memberDto.getUid(),
                memberDto.getLoginType(),
                memberDto.getNickname(),
                memberDto.getEmail(),
                accessToken,
                refreshToken);
    }
}
