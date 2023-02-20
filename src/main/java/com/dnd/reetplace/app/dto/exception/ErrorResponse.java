package com.dnd.reetplace.app.dto.exception;

public record ErrorResponse(
        Integer code,
        String message
) {
}
