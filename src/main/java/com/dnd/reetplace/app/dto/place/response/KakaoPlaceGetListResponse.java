package com.dnd.reetplace.app.dto.place.response;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoPlaceGetListResponse {
    private List<KakaoPlaceGetResponse> documents;
}
