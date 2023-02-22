package com.dnd.reetplace.app.dto.member.response;

import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.type.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long memberId;
    private String uid;
    private LoginType loginType;
    private String nickname;

    public static MemberResponse from(MemberDto dto) {
        return new MemberResponse(dto.getId(), dto.getUid(), dto.getLoginType(), dto.getNickname());
    }
}
