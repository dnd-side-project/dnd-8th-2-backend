package com.dnd.reetplace.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
    private final Throwable cause;

    public CustomException(HttpStatus httpStatus) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass()).orElse(ExceptionType.UNHANDLED);
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
        this.cause = null;
    }

    public CustomException(HttpStatus httpStatus, String optionalMessage) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass()).orElse(ExceptionType.UNHANDLED);
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage() + " " + optionalMessage;
        this.cause = null;
    }

    public CustomException(HttpStatus httpStatus, Throwable cause) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass()).orElse(ExceptionType.UNHANDLED);
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
        this.cause = cause;
    }

    public CustomException(HttpStatus httpStatus, String optionalMessage, Throwable cause) {
        ExceptionType exceptionType = ExceptionType.from(this.getClass()).orElse(ExceptionType.UNHANDLED);
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage() + " " + optionalMessage;
        this.cause = cause;
    }
}
