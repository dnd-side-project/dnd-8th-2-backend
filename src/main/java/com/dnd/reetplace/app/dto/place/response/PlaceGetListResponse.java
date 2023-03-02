package com.dnd.reetplace.app.dto.place.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceGetListResponse {
    private List<PlaceGetResponse> contents;

    public static PlaceGetListResponse of(List<PlaceGetResponse> contents) {
        return new PlaceGetListResponse(contents);
    }
}
