package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.member.response.MemberResponse;
import com.dnd.reetplace.app.service.MemberService;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자", description = "사용자 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "로그인한 사용자 정보 조회",
            description = "로그인한 사용자의 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getLoginMemberInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        MemberDto dto = memberService.getMemberInfo(memberDetails.getId());
        return ResponseEntity.ok(MemberResponse.from(dto));
    }
}
