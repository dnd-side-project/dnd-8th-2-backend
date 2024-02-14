package com.dnd.reetplace.app.domain;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE search SET deleted_at = CURRENT_TIMESTAMP WHERE search_id = ?")
@Table(name = "search")
@Entity
public class Search extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String query;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime deletedAt;

    @Builder
    private Search(String query, Member member) {
        this.query = query;
        this.member = member;
    }

    public void modifyUpdateAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
