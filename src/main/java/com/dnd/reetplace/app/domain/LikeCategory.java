package com.dnd.reetplace.app.domain;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.converter.LikeSubCategoryConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE like_category SET deleted_at = CURRENT_TIMESTAMP WHERE like_category_id = ?")
@Entity
public class LikeCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_category_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlaceCategory category;

    @Column(nullable = false)
    @Convert(converter = LikeSubCategoryConverter.class)
    private List<PlaceSubCategory> subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime deletedAt;

    @Builder
    private LikeCategory(PlaceCategory category, List<PlaceSubCategory> subCategory, Member member) {
        this.category = category;
        this.subCategory = subCategory;
        this.member = member;
    }

    public void updateSubCategory(List<PlaceSubCategory> subCategory) {
        this.subCategory = subCategory;
    }
}
