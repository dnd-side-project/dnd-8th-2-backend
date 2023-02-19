package com.dnd.reetplace.global.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Optional;

/**
 * <p>Spring, Java 등에서 제공하는 exception에 대해 custom error code, error message를 정의하기 위한 class.
 * <br>직접 정의한, {@link CustomException}을 상속받은 exception에 대한 정보는 {@link CustomExceptionType}에 작성하도록 한다.
 *
 * <ul>
 *     <li>1000 ~ 1299: 일반 예외. 아래 항목에 해당하지 않는 대부분의 예외가 여기에 해당한다</li>
 *     <li>13XX: API/Controller 관련 예외</li>
 *     <li>14XX: DB 관련 예외</li>
 * </ul>
 *
 * @see CustomExceptionType
 */
@Slf4j
@Getter
public enum GlobalExceptionType {

    UNHANDLED(1000, "알 수 없는 서버 에러가 발생했습니다."),
    METHOD_ARGUMENT_NOT_VALID(1001, "요청 데이터가 잘못되었습니다.", MethodArgumentNotValidException.class),
    CONSTRAINT_VIOLATION(1002, "요청 데이터가 잘못되었습니다.", ConstraintViolationException.class),
    HTTP_MESSAGE_NOT_READABLE(1300, "처리할 수 없는 요청입니다. 요청 정보가 잘못되지는 않았는지 확인해주세요.", HttpMessageNotReadableException.class),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(1301, "지원하지 않는 요청 방식입니다.", HttpRequestMethodNotSupportedException.class),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(1302, "Client에서 허용된 응답을 만들어 낼 수 없습니다.", HttpMediaTypeNotAcceptableException.class),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(1303, "허용되지 않는 요청 데이터 타입입니다.", HttpMediaTypeNotSupportedException.class),
    MISSING_SERVLET_REQUEST_PARAMETER(1304, "필요한 request parameter가 없습니다.", MissingServletRequestParameterException.class),
    ;

    private final Integer code;
    private final String message;
    private final Class<? extends Exception> type;

    GlobalExceptionType(int code, String message) {
        this(code, message, null);
    }

    GlobalExceptionType(int code, String message, Class<? extends Exception> type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public static GlobalExceptionType from(Class<? extends Exception> classType) {
        Optional<GlobalExceptionType> exceptionType = Arrays.stream(values())
                .filter(ex -> ex.getType() != null && ex.getType().equals(classType))
                .findFirst();

        if (exceptionType.isEmpty()) {
            log.error("정의되지 않은 exception이 발생하였습니다. Type of exception={}", classType);
        }

        return exceptionType.orElse(UNHANDLED);
    }
}
