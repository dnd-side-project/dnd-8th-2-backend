package com.dnd.reetplace.app.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookmarkSearchSort {

    LATEST("최신순"),
    POPULARITY("인기순");

    private final String description;
}
