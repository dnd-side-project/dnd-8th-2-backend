package com.dnd.reetplace.global.exception.auth;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class RefreshTokenNotFoundException extends NotFoundException {

    /**
     * 찾지 못한 refresh token을 출력한다.
     *
     * @param refreshToken 조회시도 한 refresh token
     */
    public RefreshTokenNotFoundException(String refreshToken) {
        super("refreshToken = " + refreshToken);
    }
}
