package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.type.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUidAndLoginType(String uid, LoginType loginType);
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
}
