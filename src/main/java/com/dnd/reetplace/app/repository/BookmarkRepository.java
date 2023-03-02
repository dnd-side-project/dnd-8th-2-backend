package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
