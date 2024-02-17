package com.dnd.reetplace.app.dto.place.response;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@Getter
public class KakaoPlaceGetResponse {
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

    // 내부 주소 파싱용 데이터
    @JsonIgnore
    private String sido;
    @JsonIgnore
    private String sgg;

    // Native Query 파싱을 위한 생성자 (JpaResultMapper)
    public KakaoPlaceGetResponse(
            String address_name,
            String category_group_code,
            String category_name,
            Double distance,
            String id,
            String phone,
            String place_name,
            String place_url,
            String road_address_name,
            String sido,
            String sgg,
            String x,
            String y,
            String category,
            String subCategory,
            BigInteger bookmarkCount
    ) {
        this.address_name = address_name;
        this.category_group_code = category_group_code;
        this.category_name = category_name;
        this.distance = distance.toString();
        this.id = id;
        this.phone = phone;
        this.place_name = place_name;
        this.place_url = place_url;
        this.road_address_name = road_address_name;
        this.sido = sido;
        this.sgg = sgg;
        this.x = x;
        this.y = y;
        this.category = PlaceCategory.valueOf(category);
        this.subCategory = PlaceSubCategory.valueOf(subCategory);
    }

    // Native Query 파싱을 위한 생성자 (JpaResultMapper)
    public KakaoPlaceGetResponse(
            String address_name,
            String category_name,
            Double distance,
            String id,
            String phone,
            String place_name,
            String place_url,
            String road_address_name,
            String sido,
            String sgg,
            String x,
            String y,
            String category,
            String subCategory,
            BigInteger bookmarkCount
    ) {
        this.address_name = address_name;
        this.category_name = category_name;
        this.distance = distance.toString();
        this.id = id;
        this.phone = phone;
        this.place_name = place_name;
        this.place_url = place_url;
        this.road_address_name = road_address_name;
        this.sido = sido;
        this.sgg = sgg;
        this.x = x;
        this.y = y;
        this.category = PlaceCategory.valueOf(category);
        this.subCategory = PlaceSubCategory.valueOf(subCategory);
    }

    public void updateCategory(PlaceCategory category, PlaceSubCategory subCategory) {
        this.category = category;
        this.subCategory = subCategory;
    }

    // DB 내 시도 / 시군구 단위 파싱되어 있는 주소체계 카카오 응답과 맞줌
    public void parseAddress() {
        StringBuilder roadAddressName = new StringBuilder();
        StringBuilder addressName = new StringBuilder();
        if (sgg.isEmpty()) return;
        int sggLength = sgg.split(" ").length;
        if (sggLength == 1) {
            if (sgg.charAt(sgg.length() - 1) == '구') {
                roadAddressName.append(sido).append(" ").append(sgg).append(" ").append(this.road_address_name);
                addressName.append(sido).append(" ").append(sgg).append(" ").append(this.address_name);
            } else {
                roadAddressName.append(sgg).append(" ").append(this.road_address_name);
                addressName.append(sgg).append(" ").append(this.address_name);
            }
            this.road_address_name = roadAddressName.toString();
            this.address_name = addressName.toString();
        }
    }
}
