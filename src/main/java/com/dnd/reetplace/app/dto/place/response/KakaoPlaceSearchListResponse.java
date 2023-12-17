package com.dnd.reetplace.app.dto.place.response;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoPlaceSearchListResponse {
    private Meta meta;
    private List<KakaoPlaceSearchResponse> documents;
}
