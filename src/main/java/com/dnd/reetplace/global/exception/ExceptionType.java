package com.dnd.reetplace.global.exception;

import com.dnd.reetplace.app.domain.*;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.global.exception.auth.JwtTokenEmptyUnauthorizedException;
import com.dnd.reetplace.global.exception.auth.KakaoUnauthorizedException;
import com.dnd.reetplace.global.exception.auth.RefreshTokenNotFoundException;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
import com.dnd.reetplace.global.exception.type.ValidationErrorCode;
import com.dnd.reetplace.global.log.LogUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Error code 목록
 *
 * <ul>
 *     <li>1000 ~ 1999: 일반 예외. 아래 항목에 해당하지 않는 대부분의 예외가 여기에 해당한다</li>
 *     <li>120X: Validation 관련 예외</li>
 *     <li>1210 ~ 1299: 구체적인 Validation content에 대한 exception. 해당 내용은 {@link ValidationErrorCode}, {@link GlobalExceptionHandler} 참고)</li>
 *     <li>13XX: API/Controller 관련 예외</li>
 *     <li>14XX: DB 관련 예외</li>
 *     <li>15XX: 인증 관련 예외</li>
 *     <li>2000 ~ 2399: 회원({@link Member}) 관련 예외</li>
 *     <li>2400 ~ 2699: 로그인/회원가입 관련 예외</li>
 *     <li>2700 ~ 2999: 회원 설문({@link Survey}) 관련 예외</li>
 *     <li>3000 ~ 3499: 북마크({@link Bookmark}) 관련 예외</li>
 *     <li>3500 ~ 3999: 장소({@link Place}) 관련 예외</li>
 *     <li>4000 ~ 4999: 파일 업로드({@link S3File}) 관련 예외</li>
 *     <li>5000 ~ 5499: 문의({@link Question}) 관련 예외</li>
 *     <li>5500 ~ 5999: 문의 답변({@link Answer}) 관련 예외</li>
 * </ul>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Getter
public enum ExceptionType {

    /**
     * Global/Normal Exception
     */
    UNHANDLED(1000, "알 수 없는 서버 에러가 발생했습니다.", null),

    // 인증 관련 (15XX)
    ACCESS_DENIED(1500, "접근 권한이 없습니다.", null),
    AUTHENTICATION_REQUIRED(1501, "인증이 필요한 요청입니다.", null),
    JWT_INVALID_SIGNATURE_EXCEPTION(1502, "Token의 서명이 잘못되었습니다.", SignatureException.class),
    JWT_EXPIRED_EXCEPTION(1503, "Token이 만료되었습니다.", ExpiredJwtException.class),
    JWT_MALFORMED_EXCEPTION(1504, "유효하지 않은 token입니다.", MalformedJwtException.class),
    JWT_UNSUPPORTED_EXCEPTION(1505, "처리할 수 없는 token입니다.", UnsupportedJwtException.class),

    /**
     * Validation Exception
     *
     * @see ValidationErrorCode
     */
    METHOD_ARGUMENT_NOT_VALID(1200, "요청 데이터가 잘못되었습니다.", MethodArgumentNotValidException.class),
    CONSTRAINT_VIOLATION(1201, "요청 데이터가 잘못되었습니다.", ConstraintViolationException.class),

    /**
     * Spring MVC Default Exception
     */
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(1300, "지원하지 않는 요청 방식입니다.", HttpRequestMethodNotSupportedException.class),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(1301, "지원하지 않는 요청 데이터 타입입니다.", HttpMediaTypeNotSupportedException.class),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(1302, "요청한 데이터 타입으로 응답을 만들어 낼 수 없습니다.", HttpMediaTypeNotAcceptableException.class),
    MISSING_PATH_VARIABLE(1303, "필요로 하는 path variable이 누락 되었습니다.", MissingPathVariableException.class),
    MISSING_SERVLET_REQUEST_PARAMETER(1304, "필요로 하는 request parameter가 누락 되었습니다.", MissingServletRequestParameterException.class),
    MISSING_REQUEST_HEADER(1305, "필요로 하는 request header가 누락 되었습니다.", MissingRequestHeaderException.class),
    SERVLET_REQUEST_BINDING(1306, "복구 불가능한 fatal binding exception이 발생했습니다.", ServletRequestBindingException.class),
    CONVERSION_NOT_SUPPORTED(1307, "Bean property에 대해 적절한 editor 또는 convertor를 찾을 수 없습니다.", ConversionNotSupportedException.class),
    TYPE_MISMATCH(1308, "Bean property를 설정하던 중 type mismatch로 인한 예외가 발생했습니다.", TypeMismatchException.class),
    HTTP_MESSAGE_NOT_READABLE(1309, "읽을 수 없는 요청입니다. 요청 정보가 잘못되지는 않았는지 확인해주세요.", HttpMessageNotReadableException.class),
    HTTP_MESSAGE_NOT_WRITABLE(1310, "응답 데이터를 생성할 수 없습니다.", HttpMessageNotWritableException.class),
    MISSING_SERVLET_REQUEST_PART(1311, "multipart/form-data 형식의 요청 데이터에 대해 일부가 손실되거나 누락되었습니다.", MissingServletRequestPartException.class),
    NO_HANDLER_FOUND(1312, "알 수 없는 에러가 발생했으며, 에러를 처리할 handler를 찾지 못했습니다.", NoHandlerFoundException.class),
    ASYNC_REQUEST_TIMEOUT(1313, "요청에 대한 응답 시간이 초과되었습니다.", AsyncRequestTimeoutException.class),

    /**
     * Custom Exception
     */
    // Member
    MEMBER_UID_NOT_FOUND(2000, "UID에 해당하는 사용자를 찾을 수 없습니다.", MemberUidNotFoundException.class),

    // Auth
    KAKAO_UNAUTHORIZED(2400, "카카오 로그인에 실패했습니다. 액세스 토큰이 유효한지 확인해주세요.", KakaoUnauthorizedException.class),
    REFRESH_TOKEN_NOT_FOUND(2401, "존재하지 않는 Refresh Token입니다.", RefreshTokenNotFoundException.class),
    JWT_TOKEN_EMPTY_UNAUTHORIZED(2402, "JWT Token이 포함되어 있지 않습니다.", JwtTokenEmptyUnauthorizedException.class),
    ;

    private final Integer code;
    private final String message;
    private final Class<? extends Exception> type;

    public static ExceptionType from(Class<? extends Exception> classType) {
        Optional<ExceptionType> exceptionType = Arrays.stream(values())
                .filter(ex -> ex.getType() != null && ex.getType().equals(classType))
                .findFirst();

        if (exceptionType.isEmpty()) {
            log.error("[{}] 정의되지 않은 exception이 발생하였습니다. Type of exception={}", LogUtils.getLogTraceId(), classType);
        }

        return exceptionType.orElse(UNHANDLED);
    }
}
