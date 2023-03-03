package com.dnd.reetplace.app.dto.place.response;

import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.type.BookmarkType;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceSearchResponse {

    @Schema(description = "Kakao에서 등록된 장소 id 값", example = "585651800")
    private String kakaoPid;

    @Schema(description = "이름", example = "레드버튼 신촌점")
    private String name;

    @Schema(description = "상세 페이지 URL", example = "http://place.map.kakao.com/1520672825")
    private String url;

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

    @Schema(description = "카카오 내 카테고리 분류", example = "가정,생활 > 문구,사무용품")
    private String kakaoCategoryName;

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
    private PlaceSubCategory subCategory;

    @Schema(description = "대표 전화 번호", example = "02-363-3799")
    private String phone;

    @Schema(description = "지번 주소", example = "서울 서대문구 창천동 18-31")
    private String lotNumberAddress;

    @Schema(description = "도로명 주소", example = "서울 서대문구 연세로 8")
    private String roadAddress;

    @Schema(description = "위도", example = "37.5561776340198")
    private String lat;

    @Schema(description = "경도", example = "126.93713158887188")
    private String lng;

    @Schema(description = "<p>북마크 종류. 목록은 다음과 같음</p>" +
            "<ul>" +
            "<li>WANT - 가보고 싶어요</li>" +
            "<li>GONE - 다녀왔어요</li>" +
            "<li>북마크가 없을 시 null</li>" +
            "</ul>",
            example = "WANT")
    private BookmarkType type;

    @Schema(description = "북마크 고유 id값 (북마크 없을 시 null)", example = "1")
    private Long bookmarkId;

    @Schema(description = "릿플점수 (북마크 없을 시 null)", example = "3")
    private Short rate;

    public static PlaceSearchResponse of(
            KakaoPlaceSearchResponse kakaoResponse,
            BookmarkType bookmarkType,
            Long bookmarkId,
            Short rate
    ) {
        PlaceCategoryGroupCode categoryGroupCode;
        try {
            categoryGroupCode =
                    PlaceCategoryGroupCode.valueOf(kakaoResponse.getCategory_group_code());
        } catch (Exception e) {
            categoryGroupCode = null;
        }
        PlaceCategory category = null;
        PlaceSubCategory subCategory = null;

        for (PlaceSubCategory sub : PlaceSubCategory.values()) {
            if (kakaoResponse.getCategory_name().contains(sub.getDescription())) {
                subCategory = sub;
                category = sub.getMainCategory();
                break;
            }
        }
        if (kakaoResponse.getCategory_name().contains("멕시칸") || kakaoResponse.getCategory_name().contains("아시아")) {
            subCategory = PlaceSubCategory.FOOD_WORLD;
            category = PlaceCategory.FOOD;
        }
        if (kakaoResponse.getCategory_name().contains("치킨")) {
            subCategory = PlaceSubCategory.FOOD_COOKING_BAR;
            category = PlaceCategory.FOOD;
        }

        return new PlaceSearchResponse(
                kakaoResponse.getId(),
                kakaoResponse.getPlace_name(),
                kakaoResponse.getPlace_url(),
                categoryGroupCode,
                kakaoResponse.getCategory_name(),
                category,
                subCategory,
                kakaoResponse.getPhone(),
                kakaoResponse.getAddress_name(),
                kakaoResponse.getRoad_address_name(),
                kakaoResponse.getY(),
                kakaoResponse.getX(),
                bookmarkType,
                bookmarkId,
                rate
        );
    }
}
