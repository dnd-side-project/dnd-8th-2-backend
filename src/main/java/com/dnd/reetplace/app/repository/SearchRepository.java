package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {

    @Query("select count(s.id) from Search s where s.member.id = :memberId and s.deletedAt is null")
    int countByMemberId(Long memberId);
}
