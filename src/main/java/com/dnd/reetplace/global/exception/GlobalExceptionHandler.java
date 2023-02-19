package com.dnd.reetplace.global.exception;

import com.dnd.reetplace.app.dto.exception.ErrorResponse;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import static com.dnd.reetplace.global.log.LogUtils.getLogTraceId;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException e) {
        log.error("[{}] CustomException {}:", getLogTraceId(), getExceptionStackTrace(e));

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("[{}] Validation Exception: {}", getLogTraceId(), getExceptionStackTrace(ex));

        GlobalExceptionType exceptionType = GlobalExceptionType.METHOD_ARGUMENT_NOT_VALID;
        String errorMessage = exceptionType.getErrorMessage() + " " + ex.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(exceptionType.getErrorCode(), errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationExceptionHandle(ConstraintViolationException ex) {
        log.error("[{}] Validation Exception: {}", getLogTraceId(), getExceptionStackTrace(ex));

        GlobalExceptionType exceptionType = GlobalExceptionType.CONSTRAINT_VIOLATION;
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(exceptionType.getErrorCode(), exceptionType.getErrorMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("[{}] Basic Exception: {}", getLogTraceId(), getExceptionStackTrace(ex));

        GlobalExceptionType exceptionType = GlobalExceptionType.from(ex.getClass());
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exceptionType.getErrorCode(), exceptionType.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandle(Exception e) {
        log.error("[{}] UnHandled Exception: {}", getLogTraceId(), getExceptionStackTrace(e));

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
