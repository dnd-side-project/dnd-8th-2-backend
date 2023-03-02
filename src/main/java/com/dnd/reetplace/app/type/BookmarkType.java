package com.dnd.reetplace.app.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookmarkType {
    WANT("가보고싶어요"),
    DONE("다녀왔어요");

    private final String description;
}
