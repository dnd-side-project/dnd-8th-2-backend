package com.dnd.reetplace.app.domain.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceSubCategory {

    FOOD_KOREAN("한식"),
    FOOD_CHINESE("중식"),
    FOOD_JAPANESE("일식"),
    FOOD_WESTERN("양식"),
    FOOD_WORLD("세계음식(멕시칸, 브라질, 아시안 음식)"),
    FOOD_COOKING_BAR("호프,요리주점"),
    FOOD_IZAKAYA("일본식주점"),
    FOOD_STREET_TENT_RESTAURANT("포장마차"),
    FOOD_WINE_BAR("와인바"),
    FOOD_COCKTAIL_BAR("칵테일바"),

    ACTIVITY_BOWLING("볼링장"),
    ACTIVITY_PC("피시방"),
    ACTIVITY_BILLIARDS("당구장"),
    ACTIVITY_KARAOKE("노래방"),
    ACTIVITY_COIN_KARAOKE("코인 노래방"),
    ACTIVITY_BOARD_GAME("보드게임 카페"),
    ACTIVITY_PARK("공원"),

    PHOTO_BOOTH("즉석사진,포토부스"),
    PHOTO_LIFE_FOUR_CUT("인생네컷"),
    PHOTO_PHOTO_SIGNATURE("포토 시그니쳐"),
    PHOTO_HARU_FILM("하루필름"),
    PHOTO_MONO_MANSION("모노맨션"),
    PHOTO_RGB_PHOTO("RGB 포토"),
    PHOTO_PHOTOISM("포토이즘"),
    PHOTO_OTHERS("기타 포토부스"),

    SHOPPING_DEPARTMENT_STORE("백화점"),
    SHOPPING_MART("마트"),
    SHOPPING_SUPERMARKET("슈퍼마켓"),
    SHOPPING_MARKET("시장"),

    CAFE("카페"),
    CAFE_BOOK("북카페"),
    CAFE_CARTOON("만화카페"),
    CAFE_DESERT("디저트카페"),
    CAFE_FRESH_FRUIT("생과일전문점"),

    CULTURE_CINEMA("영화관"),
    CULTURE_CAR_CINEMA("자동차극장"),
    CULTURE_CONCERT("공연장,연극극장"),
    ;

    private final String description;
}
