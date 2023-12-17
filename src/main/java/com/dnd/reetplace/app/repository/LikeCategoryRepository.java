package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.LikeCategory;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeCategoryRepository extends JpaRepository<LikeCategory, Long> {

    @Query("select lc from LikeCategory lc where lc.member.id = :memberId and lc.category = :category")
    Optional<LikeCategory> findByMemberIdAndCategory(Long memberId, PlaceCategory category);
}
