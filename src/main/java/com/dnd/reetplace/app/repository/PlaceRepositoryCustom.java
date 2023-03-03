package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.dto.place.response.KakaoPlaceGetResponse;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<KakaoPlaceGetResponse> getReetPlacePopularPlaceList(String lat, String lng);
}
