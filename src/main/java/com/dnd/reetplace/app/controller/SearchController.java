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
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "검색기록 삭제 (단건)",
            description = "로그인한 사용자의 검색기록을 삭제합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @DeleteMapping("/history/{searchId}")
    public ResponseEntity<Void> deleteSearchHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(
                    description = "삭제하고자 하는 검색기록의 id",
                    example = "1"
            ) @PathVariable Long searchId
    ) {
        searchService.deleteSearchHistory(memberDetails.getId(), searchId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "검색기록 삭제 (전체)",
            description = "로그인한 사용자의 검색기록을 삭제합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @DeleteMapping("/history")
    public ResponseEntity<Void> deleteAllSearchHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        searchService.deleteAllSearchHistory(memberDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
