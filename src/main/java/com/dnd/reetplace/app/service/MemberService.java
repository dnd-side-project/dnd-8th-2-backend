package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.global.exception.member.MemberDeletedBadRequestException;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 사용자의 정보를 반환한다.
     *
     * @param memberId 로그인한 사용자의 id
     * @return 로그인한 사용자의 정보
     */
    public MemberDto getMemberInfo(Long memberId) {
        return MemberDto.from(getMember(memberId));
    }

    /**
     * memberId에 해당하는 사용자를 반환한다.
     * memberId에 해당하는 사용자가 존재하지 않거나 탈퇴한 사용자일 시, Exception을 던진다.
     *
     * @param memberId 찾고자 하는 사용자의 id
     * @return id에 해당하는 Member Entity
     */
    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
        if (member.getDeletedAt() != null) {
            throw new MemberDeletedBadRequestException();
        }
        return member;
    }
}
