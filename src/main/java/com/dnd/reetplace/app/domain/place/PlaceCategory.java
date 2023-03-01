package com.dnd.reetplace.app.domain.place;

import lombok.Getter;

@Getter
public enum PlaceCategory {
    FOOD("식도락"),
    ACTIVITY("액티비티"),
    PHOTO_BOOTH("포토부스"),
    SHOPPING("쇼핑"),
    CAFE("카페"),
    CULTURE("문화생활"),
    ;

    private final String description;

    PlaceCategory(String description) {
        this.description = description;
    }
}
