package com.dnd.reetplace.app.dto.place.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceSearchListResponse {
    private List<PlaceSearchResponse> contents;
    private boolean lastPage;

    public static PlaceSearchListResponse of(List<PlaceSearchResponse> contents, boolean isEnd) {
        return new PlaceSearchListResponse(contents, isEnd);
    }
}
