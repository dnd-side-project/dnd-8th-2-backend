package com.dnd.reetplace.global.exception;

import java.util.List;

public record ValidationErrorResponse(
        Integer code,
        String message,
        List<ValidationErrorDetails> errors
) {
}
