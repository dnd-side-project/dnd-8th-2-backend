package com.dnd.reetplace.integration.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.service.MemberService;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.member.MemberDeletedBadRequestException;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class MemberIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자 정보 조회에 성공한다.")
    @Test
    void givenMemberId_whenGetMemberInfo_thenSuccess() {

        // given
        Member member = memberRepository.save(createMockMember(1L));

        // when
        MemberDto response = memberService.getMemberInfo(member.getId());

        // then
        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getUid()).isEqualTo(member.getUid());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
    }

    @DisplayName("사용자 정보 조회 시, 존재하지 않는 사용자일 경우 실패한다.")
    @Test
    void givenMemberId_whenGetMemberInfo_thenMemberNotFoundFail() {

        // given
        Member member = memberRepository.save(createMockMember(1L));

        // when & then
        assertThrows(MemberIdNotFoundException.class,
                () -> memberService.getMemberInfo(member.getId() + 1L));
    }

    @DisplayName("사용자 정보 조회 시, 이미 탈퇴한 사용자일 경우 실패한다.")
    @Test
    void givenMemberId_whenGetMemberInfo_thenMemberDeletedFail() {

        // given
        Member member = memberRepository.save(createMockMember(1L));
        ReflectionTestUtils.setField(member, "deletedAt", LocalDateTime.now());

        // when & then
        assertThrows(MemberDeletedBadRequestException.class,
                () -> memberService.getMemberInfo(member.getId()));
    }


    private Member createMockMember(Long id) {
        return Member.builder()
                .uid("testUid" + id)
                .loginType(LoginType.KAKAO)
                .nickname("test" + id)
                .build();
    }
}
