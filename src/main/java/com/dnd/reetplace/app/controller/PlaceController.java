package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.place.request.PlaceGetListRequest;
import com.dnd.reetplace.app.dto.place.request.PlaceSearchRequest;
import com.dnd.reetplace.app.dto.place.response.PlaceGetListResponse;
import com.dnd.reetplace.app.dto.place.response.PlaceSearchListResponse;
import com.dnd.reetplace.app.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "장소", description = "장소 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/places")
@RestController
public class PlaceController {

    private final PlaceService placeService;

    @Operation(
            summary = "장소 목록 조회",
            description = "<p>사용자의 중심좌표를 기준으로 카테고리에 해당하는 장소목록을 조회합니다.</p>" +
                    "<ul>" +
                    "<li>조회하고자 하는 카테고리를 모두 subCategory에 포함하여 요청합니다.</li>" +
                    "<li>로그인 한 사용자라면 Authorization 헤더에 Access Token을 포함하여 요청합니다.</li>" +
                    "<li>로그인 한 사용자라면 Access Token을 포함하여 요청 시 각 장소 별 북마크 여부를 확인할 수 있습니다.</li>" +
                    "</ul>"
    )
    @PostMapping
    public ResponseEntity<PlaceGetListResponse> getPlaceList(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody PlaceGetListRequest request
    ) {
        return ResponseEntity.ok(placeService.getPlaceList(httpServletRequest, request));
    }

    @Operation(
            summary = "장소 검색",
            description = "<p>검색 키워드를 기준으로 장소를 검색합니다.</p>" +
                    "<ul>" +
                    "<li>카카오 로컬 API 문서에 따라, page는 1부터 시작합니다.</li>" +
                    "<li>검색 결과는 페이지 당 최대 15개입니다.(카테고리 미분류 장소의 경우 검색결과에서 제외됩니다.)</li>" +
                    "<li>로그인 한 사용자라면 Authorization 헤더에 Access Token을 포함하여 요청합니다.</li>" +
                    "<li>로그인 한 사용자라면 Access Token을 포함하여 요청 시 각 장소 별 북마크 여부를 확인할 수 있습니다.</li>" +
                    "</ul>"
    )
    @GetMapping("/search")
    public ResponseEntity<PlaceSearchListResponse> searchPlace(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody PlaceSearchRequest request) {
        return ResponseEntity.ok(placeService.searchPlace(httpServletRequest, request));
    }
}
