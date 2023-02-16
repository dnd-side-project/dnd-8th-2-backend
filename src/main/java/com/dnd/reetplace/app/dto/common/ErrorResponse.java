package com.dnd.reetplace.app.dto.common;

import lombok.Getter;

public record ErrorResponse(
        Integer errorCode,
        String errorMessage
) {
}
