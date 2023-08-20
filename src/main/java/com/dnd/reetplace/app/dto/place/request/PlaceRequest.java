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

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
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

    private PlaceCategoryGroupCode categoryGroupCode;

    @NotBlank
    private PlaceCategory category;

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
