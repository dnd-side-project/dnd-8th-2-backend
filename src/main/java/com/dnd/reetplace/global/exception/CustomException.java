package com.dnd.reetplace.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String errorMessage;

    public CustomException(HttpStatus httpStatus) {
        CustomExceptionType exceptionType = CustomExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.errorCode = exceptionType.getErrorCode();
        this.errorMessage = exceptionType.getErrorMessage();
    }

    public CustomException(HttpStatus httpStatus, String optionalMessage) {
        CustomExceptionType exceptionType = CustomExceptionType.from(this.getClass());
        this.httpStatus = httpStatus;
        this.errorCode = exceptionType.getErrorCode();
        this.errorMessage = exceptionType.getErrorMessage() + " " + optionalMessage;
    }
}
