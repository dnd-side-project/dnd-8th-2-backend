package com.dnd.reetplace.global.exception;

import com.dnd.reetplace.app.dto.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException e) {
        log.error("[CustomException] {}", getExceptionStackTrace(e));

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
    })
    public ResponseEntity<ErrorResponse> validationExceptionHandle(Exception e) {
        log.error("[Validation Exception] {}", getExceptionStackTrace(e));

        GlobalExceptionType exceptionType = GlobalExceptionType.from(e.getClass());

        String errorMessage = exceptionType.getErrorMessage() + " ";
        if (Objects.nonNull(exceptionType.getType()) && exceptionType.getType().equals(MethodArgumentNotValidException.class)) {
            errorMessage += ((MethodArgumentNotValidException) e).getAllErrors().get(0).getDefaultMessage();
        } else {
            errorMessage += e.getMessage().split(": ")[1];
        }

        return ResponseEntity
                .status(exceptionType.getHttpStatus())
                .body(new ErrorResponse(exceptionType.getErrorCode(), errorMessage));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<ErrorResponse> globalExceptionHandle(Exception e) {
        log.error("[Global Exception] {}", getExceptionStackTrace(e));

        GlobalExceptionType exceptionType = GlobalExceptionType.from(e.getClass());

        return ResponseEntity
                .status(exceptionType.getHttpStatus())
                .body(new ErrorResponse(exceptionType.getErrorCode(), exceptionType.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandle(Exception e) {
        log.error("[UnHandled Exception] {}", getExceptionStackTrace(e));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        GlobalExceptionType.UNHANDLED.getErrorCode(),
                        GlobalExceptionType.UNHANDLED.getErrorMessage()
                ));
    }

    private String getExceptionStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return String.valueOf(stringWriter);
    }
}
