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
    private String email;

    public static MemberDto of(Long id, String uid, LoginType loginType, String nickname, String email) {
        return new MemberDto(id, uid, loginType, nickname, email);
    }

    public static MemberDto from(Member member) {
        return MemberDto.of(
                member.getId(),
                member.getUid(),
                member.getLoginType(),
                member.getNickname(),
                member.getEmail()
        );
    }
}
