package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.type.BookmarkType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMember_IdAndPlace_KakaoPid(Long memberId, String kakaoPid);

    @Query("select b from Bookmark b " +
            "join fetch b.place p " +
            "where b.member.id = :memberId")
    List<Bookmark> findAllByMember(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = {"member", "place"})
    List<Bookmark> findAllByMember_IdOrderByCreatedAtDesc(Long memberId);

    @EntityGraph(attributePaths = {"member", "place"})
    Slice<Bookmark> findByMember_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "place"})
    List<Bookmark> findAllByTypeAndMember_IdOrderByCreatedAtDesc(BookmarkType type, Long memberId);

    @EntityGraph(attributePaths = {"member", "place"})
    Slice<Bookmark> findByTypeAndMember_IdOrderByCreatedAtDesc(BookmarkType type, Long memberId, Pageable pageable);
}
