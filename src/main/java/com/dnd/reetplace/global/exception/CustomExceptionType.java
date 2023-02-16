package com.dnd.reetplace.global.exception;

import com.dnd.reetplace.app.domain.Answer;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Question;
import com.dnd.reetplace.app.domain.S3File;
import com.dnd.reetplace.app.domain.Survey;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.Place;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>직접 정의한, {@link CustomException}을 상속받은 exception에 대한 정보를 정의하는 class.
 *     <br>Custom exception이 아닌 exception에 대한 정보는 {@link GlobalExceptionType}에 작성한다.
 *
 * <ul>
 *     <li>2000 ~ 2399: 회원({@link Member}) 관련 예외</li>
 *     <li>2400 ~ 2699: 로그인/회원가입 관련 예외</li>
 *     <li>2700 ~ 2999: 회원 설문({@link Survey}) 관련 예외</li>
 *     <li>3000 ~ 3499: 북마크({@link Bookmark}) 관련 예외</li>
 *     <li>3500 ~ 3999: 장소({@link Place}) 관련 예외</li>
 *     <li>4000 ~ 4999: 파일 업로드({@link S3File}) 관련 예외</li>
 *     <li>5000 ~ 5499: 문의({@link Question}) 관련 예외</li>
 *     <li>5500 ~ 5999: 문의 답변({@link Answer}) 관련 예외</li>
 * </ul>
 *
 * @see GlobalExceptionType
 */
@Slf4j
@Getter
public enum CustomExceptionType {

    UNHANDLED(1000, "알 수 없는 서버 에러가 발생했습니다."),
    ;

    private final Integer errorCode;
    private final String errorMessage;
    private final Class<? extends CustomException> type;

    CustomExceptionType(int errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }

    CustomExceptionType(int errorCode, String errorMessage, Class<? extends CustomException> type) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.type = type;
    }

    public static CustomExceptionType from(Class<? extends CustomException> classType) {
        Optional<CustomExceptionType> exceptionType = Arrays.stream(values())
                .filter(ex -> ex.getType() != null && ex.getType().equals(classType))
                .findFirst();

        if (exceptionType.isEmpty()) {
            log.error("정의되지 않은 custom exception이 발생하였습니다. Type of exception={}", classType);
        }

        return exceptionType.orElse(UNHANDLED);
    }
}
