package com.dnd.reetplace.app.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceSubCategory {

    REET_PLACE_POPULAR("릿플인기", PlaceCategory.REET_PLACE_POPULAR),

    FOOD_KOREAN("한식", PlaceCategory.FOOD),
    FOOD_CHINESE("중식", PlaceCategory.FOOD),
    FOOD_JAPANESE("일식", PlaceCategory.FOOD),
    FOOD_WESTERN("양식", PlaceCategory.FOOD),
    FOOD_WORLD("세계음식(멕시칸, 브라질, 아시아음식)", PlaceCategory.FOOD),
    FOOD_COOKING_BAR("요리주점", PlaceCategory.FOOD),
    FOOD_IZAKAYA("일본식주점", PlaceCategory.FOOD),
    FOOD_STREET_TENT_RESTAURANT("실내포장마차", PlaceCategory.FOOD),
    FOOD_WINE_BAR("와인바", PlaceCategory.FOOD),
    FOOD_COCKTAIL_BAR("칵테일바", PlaceCategory.FOOD),

    ACTIVITY_BOWLING("볼링장", PlaceCategory.ACTIVITY),
    ACTIVITY_PC("PC방", PlaceCategory.ACTIVITY),
    ACTIVITY_BILLIARDS("당구장", PlaceCategory.ACTIVITY),
    ACTIVITY_KARAOKE("노래방", PlaceCategory.ACTIVITY),
    ACTIVITY_BOARD_GAME("보드카페", PlaceCategory.ACTIVITY),
    ACTIVITY_PARK("공원", PlaceCategory.ACTIVITY),
    ACTIVITY_ROLLER("인라인스케이트장", PlaceCategory.ACTIVITY),
    ACTIVITY_COIN_KARAOKE("코인노래방", PlaceCategory.ACTIVITY),

    PHOTO_LIFE_FOUR_CUT("인생네컷", PlaceCategory.PHOTO_BOOTH),
    PHOTO_PHOTO_SIGNATURE("포토시그니처", PlaceCategory.PHOTO_BOOTH),
    PHOTO_HARU_FILM("하루필름", PlaceCategory.PHOTO_BOOTH),
    PHOTO_SIHYUN_HADA("시현하다프레임", PlaceCategory.PHOTO_BOOTH),
    PHOTO_MONO_MANSION("모노맨션", PlaceCategory.PHOTO_BOOTH),
    PHOTO_RGB_PHOTO("RGB포토", PlaceCategory.PHOTO_BOOTH),
    PHOTO_PHOTOISM("포토이즘박스", PlaceCategory.PHOTO_BOOTH),

    SHOPPING_DEPARTMENT_STORE("백화점", PlaceCategory.SHOPPING),
    SHOPPING_MART("마트", PlaceCategory.SHOPPING),
    SHOPPING_MARKET("시장", PlaceCategory.SHOPPING),

    CAFE_BOOK("북카페", PlaceCategory.CAFE),
    CAFE_CARTOON("만화카페", PlaceCategory.CAFE),
    CAFE_DESERT("디저트카페", PlaceCategory.CAFE),
    CAFE("카페", PlaceCategory.CAFE),
    CAFE_FRESH_FRUIT("생과일전문점", PlaceCategory.CAFE),

    CULTURE_CINEMA("영화관", PlaceCategory.CULTURE),
    CULTURE_CAR_CINEMA("자동차극장", PlaceCategory.CULTURE),
    CULTURE_CONCERT("연극극장", PlaceCategory.CULTURE),
    ;

    private final String description;
    private final PlaceCategory mainCategory;
}
