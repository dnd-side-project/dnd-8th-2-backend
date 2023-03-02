package com.dnd.reetplace.app.dto.member;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.type.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {

    private Long id;
    private String uid;
    private LoginType loginType;
    private String nickname;

    public static MemberDto of(Long id, String uid, LoginType loginType, String nickname) {
        return new MemberDto(id, uid, loginType, nickname);
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getId(),
                member.getUid(),
                member.getLoginType(),
                member.getNickname()
        );
    }
}
