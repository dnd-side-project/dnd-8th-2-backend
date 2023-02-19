package com.dnd.reetplace.global.exception;

public record ValidationErrorDetails(
        Integer code,
        String field,
        String message
) {
}
