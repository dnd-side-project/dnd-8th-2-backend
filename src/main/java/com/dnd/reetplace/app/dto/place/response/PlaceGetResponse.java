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
public class PlaceGetResponse {

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

    public static PlaceGetResponse of(
            KakaoPlaceGetResponse kakaoResponse,
            BookmarkType bookmarkType,
            Long bookmarkId
    ) {
        PlaceCategoryGroupCode categoryGroupCode;
        try {
            categoryGroupCode =
                    PlaceCategoryGroupCode.valueOf(kakaoResponse.getCategory_group_code());
        } catch (Exception e) {
            categoryGroupCode = null;
        }
        return new PlaceGetResponse(
                kakaoResponse.getId(),
                kakaoResponse.getPlace_name(),
                kakaoResponse.getPlace_url(),
                categoryGroupCode,
                kakaoResponse.getCategory_name(),
                kakaoResponse.getCategory(),
                kakaoResponse.getSubCategory(),
                kakaoResponse.getPhone(),
                kakaoResponse.getAddress_name(),
                kakaoResponse.getRoad_address_name(),
                kakaoResponse.getY(),
                kakaoResponse.getX(),
                bookmarkType,
                bookmarkId
        );
    }
}
