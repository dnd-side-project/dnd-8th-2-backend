package com.dnd.reetplace.app.domain.place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = """
        <p>하위 카테고리. 목록은 다음과 같음</p>
        <ul>
            <li><code>REET_PLACE_POPULAR</code> - 릿플 인기</li>
            <li><code>KOREAN</code> - 한식</li>
            <li><code>CHINESE</code> - 중식</li>
            <li><code>JAPANESE</code> - 일식</li>
            <li><code>WESTERN</code> - 양식</li>
            <li><code>WORLD</code> - 세계음식(멕시칸, 브라질, 아시아음식)</li>
            <li><code>COOKING_BAR</code> - 호프,요리주점</li>
            <li><code>IZAKAYA</code> - 일본식주점</li>
            <li><code>STREET_TENT_RESTAURANT</code> - 포장마차</li>
            <li><code>WINE_BAR</code> - 와인바</li>
            <li><code>COCKTAIL_BAR</code> - 칵테일바</li>
            <li><code>BOWLING</code> - 볼링장</li>
            <li><code>PC</code> - 피시방</li>
            <li><code>BILLIARDS</code> - 당구장</li>
            <li><code>KARAOKE</code> - 노래방</li>
            <li><code>BOARD_GAME</code> - 보드카페</li>
            <li><code>PARK</code> - 공원</li>
            <li><code>ROLLER</code> - 롤러장</li>
            <li><code>COIN_KARAOKE</code> - 코인노래방</li>
            <li><code>LIFE_FOUR_CUT</code> - 인생네컷</li>
            <li><code>PHOTO_SIGNATURE</code> - 포토시그니처</li>
            <li><code>HARU_FILM</code> - 하루필름</li>
            <li><code>SIHYUN_HADA</code> - 시현하다 프레임</li>
            <li><code>MONO_MANSION</code> - 모노맨션</li>
            <li><code>RGB_PHOTO</code> - RGB 포토</li>
            <li><code>PHOTOISM</code> - 포토이즘</li>
            <li><code>DEPARTMENT_STORE</code> - 백화점</li>
            <li><code>MART</code> - 마트</li>
            <li><code>MARKET</code> - 시장</li>
            <li><code>CAFE</code> - 카페</li>
            <li><code>BOOK</code> - 북카페</li>
            <li><code>CARTOON</code> - 만화카페</li>
            <li><code>DESERT</code> - 디저트카페</li>
            <li><code>FRESH_FRUIT</code> - 생과일전문점</li>
            <li><code>CINEMA</code> - 영화관</li>
            <li><code>CAR_CINEMA</code> - 자동차극장</li>
            <li><code>CONCERT</code> - 공연장,연극극장</li>
        </ul>
        """,
        example = "BOARD_GAME")
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
