package com.dnd.reetplace.app.dto.category.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LikeCategoryUpdateRequest {
    List<LikeCategoryRequest> contents;
}
