package com.dnd.reetplace.global.exception.util;

import com.dnd.reetplace.global.exception.type.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolation;

@AllArgsConstructor
public class ViolationMessageResolver {

    private final ConstraintViolation<?> violation;

    /**
     * {@code violation}에 해당하는 application error code를 return한다.
     *
     * @return violation에 해당하는 application error code
     */
    public Integer getErrorCode() {
        return ValidationErrorCode.getErrorCode(
                violation.getConstraintDescriptor()
                        .getAnnotation().annotationType().getSimpleName()
        );
    }

    /**
     * Violation exception이 발생한 filed의 filed name을 return한다.
     *
     * @return Violation exception이 발생한 filed의 filed name
     */
    public String getFieldName() {
        String propertyPath = violation.getPropertyPath().toString();
        int dotIdx = propertyPath.lastIndexOf(".");
        return propertyPath.substring(dotIdx + 1);
    }

    /**
     * 발생한 violation exception의 message(설명)을 return한다.
     *
     * @return 발생한 violation exception의 message(설명)
     */
    public String getMessage() {
        return violation.getMessage();
    }
}