package com.dnd.reetplace.global.exception.member;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class MemberIdNotFoundException extends NotFoundException {

    /**
     * 찾지 못한 사용자에 해당하는 id를 출력한다.
     *
     * @param memberId 조회시도 한 사용자의 id
     */
    public MemberIdNotFoundException(Long memberId) {
        super("memberId = " + memberId);
    }
}
