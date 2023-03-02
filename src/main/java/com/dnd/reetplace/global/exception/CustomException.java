package com.dnd.reetplace.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    public CustomException(HttpStatus httpStatus) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public CustomException(HttpStatus httpStatus, String optionalMessage) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage() + " " + optionalMessage;
    }
}
