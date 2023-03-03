package com.dnd.reetplace.global.exception.bookmark;

import com.dnd.reetplace.global.exception.common.ConflictException;

public class AlreadyMarkedPlaceException extends ConflictException {

    public AlreadyMarkedPlaceException(Long memberId, String kakaoPid) {
        super(memberId + "번 회원이 이미 마킹했던 kakao place id가 " + kakaoPid + "인 장소를 다시 북마크하고 있습니다.");
    }
}
