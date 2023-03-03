package com.dnd.reetplace.app.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceCategory {
    REET_PLACE_POPULAR("릿플인기"),
    FOOD("식도락"),
    ACTIVITY("액티비티"),
    PHOTO_BOOTH("포토부스"),
    SHOPPING("쇼핑"),
    CAFE("카페"),
    CULTURE("문화생활"),
    ;

    private final String description;
}
