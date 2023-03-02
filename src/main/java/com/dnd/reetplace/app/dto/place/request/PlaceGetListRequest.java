package com.dnd.reetplace.app.dto.place.request;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceGetListRequest {
    private String lat;
    private String lng;
    private PlaceCategory category;
    private List<PlaceSubCategory> subCategory;
}
