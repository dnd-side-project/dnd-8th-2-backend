package com.dnd.reetplace.app.dto.category.response;

import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LikeCategoryResponse {
    List<PlaceSubCategory> contents;
}
