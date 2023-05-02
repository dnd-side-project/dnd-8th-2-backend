package com.dnd.reetplace.global.exception.common;

import com.dnd.reetplace.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public abstract class InternalServerException extends CustomException {

    public InternalServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String optionalMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, optionalMessage);
    }

    public InternalServerException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public InternalServerException(String optionalMessage, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, optionalMessage, cause);
    }
}
