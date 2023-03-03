package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.dto.place.response.KakaoPlaceResponse;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<KakaoPlaceResponse> getReetPlacePopularPlaceList(String lat, String lng);
}
