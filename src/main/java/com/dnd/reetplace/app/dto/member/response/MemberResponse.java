package com.dnd.reetplace.app.dto.member.response;

import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.type.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description="사용자 고유 id", example="1")
    private Long id;

    @Schema(description = "사용자 고유 uid", example = "1284203261")
    private String uid;

    @Schema(description = "로그인한 사용자의 SNS 형식", example = "KAKAO")
    private LoginType loginType;

    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String nickname;

    public static MemberResponse from(MemberDto dto) {
        return new MemberResponse(dto.getId(), dto.getUid(), dto.getLoginType(), dto.getNickname());
    }
}
