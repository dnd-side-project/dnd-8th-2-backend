package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {

    @Query("select count(s.id) from Search s where s.member.id = :memberId and s.deletedAt is null")
    int countByMemberId(Long memberId);

    @Query("select s from Search s join fetch s.member m where s.member.id = :memberId and s.deletedAt is null order by s.updatedAt desc")
    List<Search> findByMemberIdAndDeletedAtIsNull(Long memberId);

    @Query("select s from Search s join fetch s.member m where s.id = :searchId and s.deletedAt is null")
    Optional<Search> findByIdAndDeletedAtIsNull(Long searchId);

    @Modifying
    @Query("delete from Search s where s.member.id = :memberId and s.deletedAt is null")
    void deleteAllByMemberId(Long memberId);

    Optional<Search> findByMemberIdAndQueryAndDeletedAtIsNull(Long memberId, String query);
}
