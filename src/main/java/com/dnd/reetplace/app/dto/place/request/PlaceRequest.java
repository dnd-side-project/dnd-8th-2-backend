package com.dnd.reetplace.app.dto.place.request;

import com.dnd.reetplace.app.domain.place.Address;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.domain.place.Point;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceRequest {

    @Schema(description = "Kakao에서 전달받은 장소 id", example = "585651800")
    @NotBlank
    private String kakaoPlaceId;

    @Schema(description = "이름", example = "레드버튼 신촌점")
    @NotBlank
    private String name;

    @Schema(description = "상세 페이지 URL", example = "http://place.map.kakao.com/1520672825")
    private String url;

    @Schema(description = "카카오에서 전달받은 category name", example = "음식점 > 카페 > 테마카페 > 보드카페")
    @NotBlank
    private String kakaoCategoryName;

    @Schema(description = "<p>카테고리 그룹 코드. 목록은 다음과 같음</p>" +
            "<ul>" +
            "<li>MT1 - 대형마트</li>" +
            "<li>CS2 - 편의점</li>" +
            "<li>PS3 - 어린이집, 유치원</li>" +
            "<li>SC4 - 학교</li>" +
            "<li>AC5 - 학원</li>" +
            "<li>PK6 - 주차장</li>" +
            "<li>OL7 - 주유소, 충전소</li>" +
            "<li>SW8 - 지하철역</li>" +
            "<li>BK9 - 은행</li>" +
            "<li>CT1 - 문화시설</li>" +
            "<li>AG2 - 중개업소</li>" +
            "<li>PO3 - 공공기관</li>" +
            "<li>AT4 - 관광명소</li>" +
            "<li>AD5 - 숙박</li>" +
            "<li>FD6 - 음식점</li>" +
            "<li>CE7 - 카페</li>" +
            "<li>HP8 - 병원</li>" +
            "<li>PM9 - 약국</li>" +
            "</ul>",
            example = "CE7")
    private PlaceCategoryGroupCode categoryGroupCode;

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
    @NotBlank
    private PlaceCategory category;

    @Schema(description = "<p>하위 카테고리. 목록은 다음과 같음</p>" +
            "<ul>" +
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
            example = "ACTIVITY_BOARD_GAME")
    @NotBlank
    private PlaceSubCategory subCategory;

    @Schema(description = "대표 전화 번호", example = "02-363-3799")
    private String phone;

    @Schema(description = "지번 주소", example = "서울 서대문구 창천동 18-31")
    private String lotNumberAddress;

    @Schema(description = "도로명 주소", example = "서울 서대문구 연세로 8")
    private String roadAddress;

    @Schema(description = "위도", example = "37.5561776340198")
    @NotBlank
    private String lat;

    @Schema(description = "경도", example = "126.93713158887188")
    @NotBlank
    private String lng;

    public PlaceDto toDto() {
        return PlaceDto.of(
                this.getKakaoPlaceId(),
                this.getName(),
                this.getUrl(),
                this.getKakaoCategoryName(),
                this.getCategoryGroupCode(),
                this.getCategory(),
                this.getSubCategory(),
                this.getPhone(),
                new Address(this.getLotNumberAddress(), this.getRoadAddress()),
                new Point(this.getLat(), this.getLng())
        );
    }
}
