package com.dnd.reetplace.global.exception.search;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class SearchIdNotFoundException extends NotFoundException {

    /**
     * 찾지 못한 검색기록에 해당하는 id를 출력한다.
     *
     * @param searchId 조회시도 한 검색기록 id
     */
    public SearchIdNotFoundException(Long searchId) {
        super("searchId = " + searchId);
    }
}
