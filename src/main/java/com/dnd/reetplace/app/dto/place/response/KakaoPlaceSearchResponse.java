package com.dnd.reetplace.app.dto.place.response;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
@Getter
public class KakaoPlaceSearchResponse {
    private String addressName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String distance;
    private String id;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;
    private String x;
    private String y;
}
