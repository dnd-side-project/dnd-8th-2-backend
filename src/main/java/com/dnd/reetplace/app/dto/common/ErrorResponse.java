package com.dnd.reetplace.app.dto.common;

public record ErrorResponse(
        Integer errorCode,
        String errorMessage
) {
}
