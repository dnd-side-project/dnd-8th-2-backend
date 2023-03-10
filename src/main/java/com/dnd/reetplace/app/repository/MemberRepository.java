package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.type.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUidAndLoginTypeAndDeletedAtIsNull(String uid, LoginType loginType);
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
}
