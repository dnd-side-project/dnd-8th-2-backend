package com.dnd.reetplace.app.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceSubCategory {

    REET_PLACE_POPULAR("릿플인기", PlaceCategory.REET_PLACE_POPULAR),

    KOREAN("한식", PlaceCategory.FOOD),
    CHINESE("중식", PlaceCategory.FOOD),
    JAPANESE("일식", PlaceCategory.FOOD),
    WESTERN("양식", PlaceCategory.FOOD),
    WORLD("세계음식(멕시칸, 브라질, 아시아음식)", PlaceCategory.FOOD),
    COOKING_BAR("요리주점", PlaceCategory.FOOD),
    IZAKAYA("일본식주점", PlaceCategory.FOOD),
    STREET_TENT_RESTAURANT("실내포장마차", PlaceCategory.FOOD),
    WINE_BAR("와인바", PlaceCategory.FOOD),
    COCKTAIL_BAR("칵테일바", PlaceCategory.FOOD),

    BOWLING("볼링장", PlaceCategory.ACTIVITY),
    PC("PC방", PlaceCategory.ACTIVITY),
    BILLIARDS("당구장", PlaceCategory.ACTIVITY),
    KARAOKE("노래방", PlaceCategory.ACTIVITY),
    BOARD_GAME("보드카페", PlaceCategory.ACTIVITY),
    PARK("공원", PlaceCategory.ACTIVITY),
    ROLLER("인라인스케이트장", PlaceCategory.ACTIVITY),
    COIN_KARAOKE("코인노래방", PlaceCategory.ACTIVITY),

    LIFE_FOUR_CUT("인생네컷", PlaceCategory.PHOTO_BOOTH),
    PHOTO_SIGNATURE("포토시그니처", PlaceCategory.PHOTO_BOOTH),
    HARU_FILM("하루필름", PlaceCategory.PHOTO_BOOTH),
    SIHYUN_HADA("시현하다프레임", PlaceCategory.PHOTO_BOOTH),
    MONO_MANSION("모노맨션", PlaceCategory.PHOTO_BOOTH),
    RGB_PHOTO("RGB포토", PlaceCategory.PHOTO_BOOTH),
    PHOTOISM("포토이즘박스", PlaceCategory.PHOTO_BOOTH),

    DEPARTMENT_STORE("백화점", PlaceCategory.SHOPPING),
    MART("마트", PlaceCategory.SHOPPING),
    MARKET("시장", PlaceCategory.SHOPPING),

    BOOK("북카페", PlaceCategory.CAFE),
    CARTOON("만화카페", PlaceCategory.CAFE),
    DESERT("디저트카페", PlaceCategory.CAFE),
    CAFE("카페", PlaceCategory.CAFE),
    FRESH_FRUIT("생과일전문점", PlaceCategory.CAFE),

    CINEMA("영화관", PlaceCategory.CULTURE),
    CAR_CINEMA("자동차극장", PlaceCategory.CULTURE),
    CONCERT("연극극장", PlaceCategory.CULTURE),
    ;

    private final String description;
    private final PlaceCategory mainCategory;
}
