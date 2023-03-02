package com.dnd.reetplace.app.dto.place.response;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import lombok.Getter;

@Getter
public class KakaoPlaceResponse {
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String x;
    private String y;
    private PlaceCategory category;
    private PlaceSubCategory subCategory;

    public void updateCategory(PlaceCategory category, PlaceSubCategory subCategory) {
        this.category = category;
        this.subCategory = subCategory;
    }
}
