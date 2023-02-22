package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.member.response.MemberResponse;
import com.dnd.reetplace.app.service.MemberService;
import com.dnd.reetplace.global.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getLoginMemberInfo(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        MemberDto dto = memberService.getMemberInfo(memberDetails.getUid(), memberDetails.getLoginType());
        return ResponseEntity.ok(MemberResponse.from(dto));
    }
}
