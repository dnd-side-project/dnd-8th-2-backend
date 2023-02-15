package com.dnd.reetplace.app.domain.place;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE place SET deleted_at = CURRENT_TIMESTAMP WHERE place_id = ?")
@Entity
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(nullable = false)
    private String kakaoPid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private PlaceCategoryGroupCode categoryGroupCode;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private String subCategoryName;

    @Embedded
    private Address address;

    @Embedded
    private Point point;

    private LocalDateTime deleted_at;

    @Builder
    private Place(
            String kakaoPid,
            String name,
            String url,
            PlaceCategoryGroupCode categoryGroupCode,
            String categoryName,
            String subCategoryName,
            String lotNumberAddress,
            String roadAddress,
            String lat,
            String lng
    ) {
        this.kakaoPid = kakaoPid;
        this.name = name;
        this.url = url;
        this.categoryGroupCode = categoryGroupCode;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.address = new Address(lotNumberAddress, roadAddress);
        this.point = new Point(lat, lng);
    }
}
