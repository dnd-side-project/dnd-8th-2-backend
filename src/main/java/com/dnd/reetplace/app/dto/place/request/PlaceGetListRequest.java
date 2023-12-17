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
            "<li>KOREAN - 한식</li>" +
            "<li>CHINESE - 중식</li>" +
            "<li>JAPANESE - 일식</li>" +
            "<li>WESTERN - 양식</li>" +
            "<li>WORLD - 세계음식(멕시칸, 브라질, 아시아음식)</li>" +
            "<li>COOKING_BAR - 호프,요리주점</li>" +
            "<li>IZAKAYA - 일본식주점</li>" +
            "<li>STREET_TENT_RESTAURANT - 포장마차</li>" +
            "<li>WINE_BAR - 와인바</li>" +
            "<li>COCKTAIL_BAR - 칵테일바</li>" +
            "<li>BOWLING - 볼링장</li>" +
            "<li>PC - 피시방</li>" +
            "<li>BILLIARDS - 당구장</li>" +
            "<li>KARAOKE - 노래방</li>" +
            "<li>BOARD_GAME - 보드카페</li>" +
            "<li>PARK - 공원</li>" +
            "<li>ROLLER - 롤러장</li>" +
            "<li>COIN_KARAOKE - 코인노래방</li>" +
            "<li>LIFE_FOUR_CUT - 인생네컷</li>" +
            "<li>PHOTO_SIGNATURE - 포토시그니처</li>" +
            "<li>HARU_FILM - 하루필름</li>" +
            "<li>SIHYUN_HADA - 시현하다 프레임</li>" +
            "<li>MONO_MANSION - 모노맨션</li>" +
            "<li>RGB_PHOTO - RGB 포토</li>" +
            "<li>PHOTOISM - 포토이즘</li>" +
            "<li>DEPARTMENT_STORE - 백화점</li>" +
            "<li>MART - 마트</li>" +
            "<li>MARKET - 시장</li>" +
            "<li>CAFE - 카페</li>" +
            "<li>BOOK - 북카페</li>" +
            "<li>CARTOON - 만화카페</li>" +
            "<li>DESERT - 디저트카페</li>" +
            "<li>FRESH_FRUIT - 생과일전문점</li>" +
            "<li>CINEMA - 영화관</li>" +
            "<li>CAR_CINEMA - 자동차극장</li>" +
            "<li>CONCERT - 공연장,연극극장</li>" +
            "</ul>",
            type = "list", example = "[\"ACTIVITY_PC\", \"ACTIVITY_KARAOKE\", \"ACTIVITY_PARK\"]")
    private List<PlaceSubCategory> subCategory;

    public void updateSubCategoryForLoginMember(List<PlaceSubCategory> subCategory) {
        this.subCategory = subCategory;
    }
}
