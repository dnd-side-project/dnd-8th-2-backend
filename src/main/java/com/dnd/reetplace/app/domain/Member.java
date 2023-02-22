package com.dnd.reetplace.app.domain;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.type.LoginType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE member_id = ?")
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String email;

    @Column(nullable = false)
    private String nickname;

    private LocalDateTime deletedAt;

    @Builder
    private Member(Long id, String uid, LoginType loginType, String email, String nickname) {
        this.id = id;
        this.uid = uid;
        this.loginType = loginType;
        this.email = email;
        this.nickname = nickname;
    }
}
