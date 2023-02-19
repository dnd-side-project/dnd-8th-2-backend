package com.dnd.reetplace.global.exception.member;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class MemberUidNotFoundException extends NotFoundException {

    /**
     * 찾지못한 사용자에 해당하는 uid를 출력한다.
     *
     * @param uid 조회시도 한 사용자의 uid
     */
    public MemberUidNotFoundException(String uid) {
        super("uid = " + uid);
    }
}
