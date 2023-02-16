package com.dnd.reetplace.global.exception.common;

import com.dnd.reetplace.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends CustomException {

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String optionalMessage) {
        super(HttpStatus.NOT_FOUND, optionalMessage);
    }
}
