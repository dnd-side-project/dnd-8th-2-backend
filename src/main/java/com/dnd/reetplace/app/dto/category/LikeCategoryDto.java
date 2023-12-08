package com.dnd.reetplace.app.dto.category;

import com.dnd.reetplace.app.domain.LikeCategory;
import com.dnd.reetplace.app.domain.place.*;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LikeCategoryDto {

    private Long id;
    private PlaceCategory category;
    private List<PlaceSubCategory> subCategory;
    private MemberDto member;

    public static LikeCategoryDto from(LikeCategory entity) {
        return of(
                entity.getId(),
                entity.getCategory(),
                entity.getSubCategory(),
                MemberDto.from(entity.getMember())
        );
    }

    public static LikeCategoryDto of(Long id, PlaceCategory category, List<PlaceSubCategory> subCategory, MemberDto member) {
        return new LikeCategoryDto(id, category, subCategory, member);
    }
}
