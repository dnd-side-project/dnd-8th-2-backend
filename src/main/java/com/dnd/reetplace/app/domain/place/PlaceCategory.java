package com.dnd.reetplace.app.domain.place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = """
        <p> 장소 카테고리. 목록은 다음과 같다.</p>
        <ul>
            <li><code>FOOD</code> - 식도락</li>
            <li><code>ACTIVITY</code> - 액티비티</li>
            <li><code>PHOTO_BOOTH</code> - 포토부스</li>
            <li><code>SHOPPING</code> - 쇼핑</li>
            <li><code>CAFE</code> - 카페</li>
            <li><code>CULTURE</code> - 문화생활</li>
        </ul>
        """,
        example = "ACTIVITY")
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
