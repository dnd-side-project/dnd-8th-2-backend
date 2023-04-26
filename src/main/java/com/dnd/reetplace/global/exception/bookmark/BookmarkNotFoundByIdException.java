package com.dnd.reetplace.global.exception.bookmark;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class BookmarkNotFoundByIdException extends NotFoundException {
    public BookmarkNotFoundByIdException(Long bookmarkId) {
        super("bookmarkId=" + bookmarkId);
    }
}
