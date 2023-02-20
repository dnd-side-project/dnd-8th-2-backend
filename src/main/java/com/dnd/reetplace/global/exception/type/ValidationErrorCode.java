package com.dnd.reetplace.global.exception.type;

import com.dnd.reetplace.global.exception.ExceptionType;
import lombok.Getter;

@Getter
public enum ValidationErrorCode {
    AssertFalse,
    AssertTrue,
    DecimalMax,
    DecimalMin,
    Digits,
    Email,
    Future,
    FutureOrPresent,
    Max,
    Min,
    Negative,
    NegativeOrZero,
    NotBlank,
    NotEmpty,
    NotNull,
    Null,
    Past,
    PastOrPresent,
    Pattern,
    Positive,
    PositiveOrZero,
    Size,
    DurationMax,
    DurationMin,
    CodePointLength,
    ConstraintComposition,
    CreditCardNumber,
    Currency,
    EAN,
    ISBN,
    Length,
    Range,
    UniqueElements,
    URL;

    /**
     * 이 수치는 {@code GlobalExceptionType} 참고
     *
     * @see ExceptionType
     */
    private static final int VALIDATION_EX_BASE_CODE = 1210;

    /**
     * Parameter로 전달된 name에 일치하는 validation error를 찾고 해당하는 application error code를 return한다.
     *
     * @param name validation error ({@code ValidationErrorCode} enum class의 constant 중 하나)
     * @return parameter로 전돨된 name에 해당하는 validation error의 application error code
     */
    public static Integer getErrorCode(String name) {
        return VALIDATION_EX_BASE_CODE + valueOf(name).ordinal();
    }
}
