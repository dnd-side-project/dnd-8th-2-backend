package com.dnd.reetplace.app.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    NORMAL("ROLE_NORMAL", "일반 사용자"),
    GUEST("ROLE_GUEST", "게스트 사용자");

    private final String key;
    private final String description;
}
