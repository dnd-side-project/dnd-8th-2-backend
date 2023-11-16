package com.dnd.reetplace.app.dto.place.response;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceSummaryResponse {

    @Schema(description = "id(PK)", example = "1")
    private Long id;

    @Schema(description = "이름", example = "레드버튼 신촌점")
    private String name;

    @Schema(description = "상세 페이지 URL", example = "http://place.map.kakao.com/1520672825")
    private String url;

    @Schema(description = "<p>카테고리. 목록은 다음과 같음</p>" +
                          "<ul>" +
                          "<li>FOOD - 식도락</li>" +
                          "<li>ACTIVITY - 액티비티</li>" +
                          "<li>PHOTO_BOOTH - 포토부스</li>" +
                          "<li>SHOPPING - 쇼핑</li>" +
                          "<li>CAFE - 카페</li>" +
                          "<li>CULTURE - 문화생활</li>" +
                          "</ul>",
            example = "ACTIVITY")
    private PlaceCategory category;

    @Schema(description = "<p>하위 카테고리. 목록은 다음과 같음</p>" +
                          "<ul>" +
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
            example = "ACTIVITY_BOARD_GAME")
    private PlaceSubCategory subCategory;

    @Schema(description = "지번 주소", example = "서울 서대문구 창천동 18-31")
    private String lotNumberAddress;

    @Schema(description = "도로명 주소", example = "서울 서대문구 연세로 8")
    private String roadAddress;

    @Schema(description = "위도", example = "37.5561776340198")
    private String lat;

    @Schema(description = "경도", example = "126.93713158887188")
    private String lng;

    public static PlaceSummaryResponse from(PlaceDto place) {
        return new PlaceSummaryResponse(
                place.getId(),
                place.getName(),
                place.getUrl(),
                place.getCategory(),
                place.getSubCategory(),
                place.getAddress().getLotNumberAddress(),
                place.getAddress().getRoadAddress(),
                place.getPoint().getLat(),
                place.getPoint().getLng()
        );
    }
}
