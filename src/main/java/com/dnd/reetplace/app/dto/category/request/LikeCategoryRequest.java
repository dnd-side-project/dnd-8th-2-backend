package com.dnd.reetplace.app.dto.category.request;

import com.dnd.reetplace.app.domain.LikeCategory;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class LikeCategoryRequest {

    @Schema(description = "상위 카테고리", example = "ACTIVITY")
    @NotBlank
    private PlaceCategory category;

    @Schema(description = "하위 카테고리 목록", type = "list", example = "[\"ACTIVITY_PC\", \"ACTIVITY_KARAOKE\", \"ACTIVITY_PARK\"]")
    @NotNull
    private List<PlaceSubCategory> subCategory;

    public LikeCategory toEntity(Member member) {
        return LikeCategory.builder()
                .category(category)
                .subCategory(subCategory)
                .member(member)
                .build();
    }
}
