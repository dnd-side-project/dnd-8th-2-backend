package com.dnd.reetplace.global.exception;

import com.dnd.reetplace.app.dto.exception.ErrorResponse;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.dnd.reetplace.global.log.LogUtils.getLogTraceId;

/**
 * <p>{@code @ExceptionHandler} method를 통해 Application에서 발생하는 모든 exception들을 처리하는 class.
 *
 * <p>이 class는 Spring MVC exception을 처리하는 {@link ResponseEntityExceptionHandler}을 상속받아 구현되었음.
 * 이 때문에 Spring MVC에서 발생할 수 있는 기본 exception들을 전부 처리하며,
 * 일부 exception의 경우 응답 형태를 재가공하여 client에게 응답한다.
 *
 * @see ResponseEntityExceptionHandler
 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException ex) {
        log.error("[{}] CustomException {}:", getLogTraceId(), getExceptionStackTrace(ex));

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage()));
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
        log.error("[{}] Spring MVC Basic Exception: {}", getLogTraceId(), getExceptionStackTrace(ex));

        GlobalExceptionType exceptionType = GlobalExceptionType.from(ex.getClass());
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exceptionType.getErrorCode(), exceptionType.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandle(Exception ex) {
        log.error("[{}] UnHandled Exception: {}", getLogTraceId(), getExceptionStackTrace(ex));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        GlobalExceptionType.UNHANDLED.getErrorCode(),
                        GlobalExceptionType.UNHANDLED.getErrorMessage()
                ));
    }

    private String getExceptionStackTrace(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return String.valueOf(stringWriter);
    }
}
