package com.dnd.reetplace.app.dto.place.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
public class PlaceSearchRequest {

    @Schema(description = "중심좌표 - 위도", example = "37.5561776340198")
    @NotEmpty
    private String lat;

    @Schema(description = "중심좌표 - 경도", example = "126.93713158887188")
    @NotEmpty
    private String lng;

    @Schema(description = "검색 키워드", example = "햄버거")
    @NotEmpty
    private String query;

    @Schema(description = "페이지 번호 (1부터 시작합니다). 기본값은 1입니다.", example = "1", defaultValue = "1")
    private int page;

    public PlaceSearchRequest() {
        this.page = 1;
    }
}