package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
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
     * @param uid 로그인한 사용자의 uid
     * @param loginType 로그인한 사용자의 login type
     * @return 로그인한 사용자의 정보
     */
    public MemberDto getMemberInfo(String uid, LoginType loginType) {
        return MemberDto.from(getMember(uid, loginType));
    }

    /**
     * uid, login type에 해당하는 사용자를 반환한다.
     * uid, login type에 해당하는 사용자가 존재하지 않을 시, Exception을 던진다.
     *
     * @param uid 찾고자 하는 사용자의 uid
     * @param loginType 찾고자 하는 사용자의 login type
     * @return 찾은 Member Entity
     */
    public Member getMember(String uid, LoginType loginType) {
        return memberRepository.findByUidAndLoginType(uid, loginType)
                .orElseThrow(() -> new MemberUidNotFoundException(uid));
    }
}
