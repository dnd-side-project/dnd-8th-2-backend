package com.dnd.reetplace.app.dto.place.request;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class PlaceGetListRequest {

    @Schema(description = "중심좌표 - 위도", example = "37.5561776340198")
    @NotEmpty
    private String lat;

    @Schema(description = "중심좌표 - 경도", example = "126.93713158887188")
    @NotEmpty
    private String lng;

    @Schema(description = "<p>카테고리. 목록은 다음과 같음</p>" +
            "<ul>" +
            "<li>REET_PLACE_POPULAR - 릿플인기</li>" +
            "<li>FOOD - 식도락</li>" +
            "<li>ACTIVITY - 액티비티</li>" +
            "<li>PHOTO_BOOTH - 포토부스</li>" +
            "<li>SHOPPING - 쇼핑</li>" +
            "<li>CAFE - 카페</li>" +
            "<li>CULTURE - 문화생활</li>" +
            "</ul>",
            example = "ACTIVITY")
    @NotNull
    private PlaceCategory category;

    @Schema(description = "<p>하위 카테고리. 목록은 다음과 같음</p>" +
            "<ul>" +
            "<li>REET_PLACE_POPULAR - 릿플인기</li>" +
            "<li>FOOD_KOREAN - 한식</li>" +
            "<li>FOOD_CHINESE - 중식</li>" +
            "<li>FOOD_JAPANESE - 일식</li>" +
            "<li>FOOD_WESTERN - 양식</li>" +
            "<li>FOOD_WORLD - 세계음식(멕시칸, 브라질, 아시아음식)</li>" +
            "<li>FOOD_COOKING_BAR - 호프,요리주점</li>" +
            "<li>FOOD_IZAKAYA - 일본식주점</li>" +
            "<li>FOOD_STREET_TENT_RESTAURANT - 포장마차</li>" +
            "<li>FOOD_WINE_BAR - 와인바</li>" +
            "<li>FOOD_COCKTAIL_BAR - 칵테일바</li>" +
            "<li>ACTIVITY_BOWLING - 볼링장</li>" +
            "<li>ACTIVITY_PC - 피시방</li>" +
            "<li>ACTIVITY_BILLIARDS - 당구장</li>" +
            "<li>ACTIVITY_KARAOKE - 노래방</li>" +
            "<li>ACTIVITY_BOARD_GAME - 보드카페</li>" +
            "<li>ACTIVITY_PARK - 공원</li>" +
            "<li>ACTIVITY_ROLLER - 롤러장</li>" +
            "<li>ACTIVITY_COIN_KARAOKE - 코인노래방</li>" +
            "<li>PHOTO_LIFE_FOUR_CUT - 인생네컷</li>" +
            "<li>PHOTO_PHOTO_SIGNATURE - 포토시그니처</li>" +
            "<li>PHOTO_HARU_FILM - 하루필름</li>" +
            "<li>PHOTO_SIHYUN_HADA - 시현하다 프레임</li>" +
            "<li>PHOTO_MONO_MANSION - 모노맨션</li>" +
            "<li>PHOTO_RGB_PHOTO - RGB 포토</li>" +
            "<li>PHOTO_PHOTOISM - 포토이즘</li>" +
            "<li>SHOPPING_DEPARTMENT_STORE - 백화점</li>" +
            "<li>SHOPPING_MART - 마트</li>" +
            "<li>SHOPPING_MARKET - 시장</li>" +
            "<li>CAFE - 카페</li>" +
            "<li>CAFE_BOOK - 북카페</li>" +
            "<li>CAFE_CARTOON - 만화카페</li>" +
            "<li>CAFE_DESERT - 디저트카페</li>" +
            "<li>CAFE_FRESH_FRUIT - 생과일전문점</li>" +
            "<li>CULTURE_CINEMA - 영화관</li>" +
            "<li>CULTURE_CAR_CINEMA - 자동차극장</li>" +
            "<li>CULTURE_CONCERT - 공연장,연극극장</li>" +
            "</ul>",
            type = "list", example = "[\"ACTIVITY_PC\", \"ACTIVITY_KARAOKE\", \"ACTIVITY_PARK\"]")
    @NotEmpty
    private List<PlaceSubCategory> subCategory;
}
