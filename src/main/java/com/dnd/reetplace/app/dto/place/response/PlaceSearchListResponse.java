package com.dnd.reetplace.app.dto.place.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceSearchListResponse {
    private List<PlaceSearchResponse> contents;

    public static PlaceSearchListResponse of(List<PlaceSearchResponse> contents) {
        return new PlaceSearchListResponse(contents);
    }
}
