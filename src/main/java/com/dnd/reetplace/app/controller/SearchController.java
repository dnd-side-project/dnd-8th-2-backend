package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.service.SearchService;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "검색 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    @Operation(
            summary = "검색기록 조회",
            description = "<p>로그인한 사용자의 검색기록을 조회합니다.</p>" +
                    "<ul>" +
                    "<li>사용자의 검색기록은 최신순으로 최대 20개까지만 저장됩니다.</li>" +
                    "<li>사용자의 검색기록은 최신순으로 정렬되어 반환됩니다.</li>" +
                    "</ul>",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/history")
    public ResponseEntity<SearchHistoryListResponse> getSearchHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        return ResponseEntity.ok(searchService.getSearchHistory(memberDetails.getId()));
    }
}
