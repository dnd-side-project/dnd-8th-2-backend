package com.dnd.reetplace.app.dto.place;

import com.dnd.reetplace.app.domain.place.*;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceDto {

    private Long id;
    private String kakaoPid;
    private String name;
    private String url;
    private PlaceCategoryGroupCode categoryGroupCode;
    private PlaceCategory category;
    private PlaceSubCategory subCategory;
    private Address address;
    private Point point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PlaceDto of(String kakaoPid, String name, String url, PlaceCategoryGroupCode categoryGroupCode, PlaceCategory category, PlaceSubCategory subCategory, Address address, Point point) {
        return of(null, kakaoPid, name, url, categoryGroupCode, category, subCategory, address, point, null, null, null);
    }

    public static PlaceDto of(Long id, String kakaoPid, String name, String url, PlaceCategoryGroupCode categoryGroupCode, PlaceCategory category, PlaceSubCategory subCategory, Address address, Point point, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new PlaceDto(id, kakaoPid, name, url, categoryGroupCode, category, subCategory, address, point, createdAt, updatedAt, deletedAt);
    }

    public static PlaceDto from(Place entity) {
        return of(
                entity.getId(),
                entity.getKakaoPid(),
                entity.getName(),
                entity.getUrl(),
                entity.getCategoryGroupCode(),
                entity.getCategory(),
                entity.getSubCategory(),
                entity.getAddress(),
                entity.getPoint(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public Place toEntity() {
        return Place.builder()
                .kakaoPid(kakaoPid)
                .name(name)
                .url(url)
                .categoryGroupCode(categoryGroupCode)
                .category(category)
                .subCategory(subCategory)
                .address(address)
                .lat(point.getLat())
                .lng(point.getLng())
                .build();
    }
}
