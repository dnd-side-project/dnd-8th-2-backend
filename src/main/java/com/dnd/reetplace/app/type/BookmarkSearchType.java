package com.dnd.reetplace.app.type;

import lombok.Getter;

@Getter
public enum BookmarkSearchType {
    ALL, WANT, GONE;

    public BookmarkType toBookmarkType() {
        return BookmarkType.valueOf(this.name());
    }
}
