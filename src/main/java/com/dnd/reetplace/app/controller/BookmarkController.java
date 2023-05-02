package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkCreateRequest;
import com.dnd.reetplace.app.dto.bookmark.response.BookmarkResponse;
import com.dnd.reetplace.app.dto.bookmark.response.NumOfBookmarksResponse;
import com.dnd.reetplace.app.service.BookmarkService;
import com.dnd.reetplace.app.service.ScrapService;
import com.dnd.reetplace.app.type.BookmarkSearchSort;
import com.dnd.reetplace.app.type.BookmarkSearchType;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Tag(name = "장소 북마크", description = "장소 북마크 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ScrapService scrapService;

    @Operation(
            summary = "북마크 생성",
            description = "북마크 할 장소 및 북마크 내용을 기반으로 북마크를 생성합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping
    public ResponseEntity<BookmarkResponse> save(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody BookmarkCreateRequest request
    ) {
        String placeThumbnailUrl = scrapService.getPlaceThumbnailUrl(request.getPlace().getKakaoPlaceId());
        BookmarkDto bookmarkDto = bookmarkService.save(memberDetails.getId(), request.toDto(placeThumbnailUrl));

        return ResponseEntity
                .created(URI.create("/api/bookmarks/" + bookmarkDto.getId()))
                .body(BookmarkResponse.from(bookmarkDto));
    }

    @Operation(
            summary = "북마크 개수 조회",
            description = "북마크 개수를 조회합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/counts")
    public NumOfBookmarksResponse getNumOfBookmarks(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        return bookmarkService.getNumOfBookmarks(memberDetails.getId());
    }

    @Operation(
            summary = "북마크 리스트 조회",
            description = "북마크 내역을 불러옵니다. 한 페이지에 20개의 데이터가 응답됩니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping
    public ResponseEntity<Slice<BookmarkResponse>> searchBookmarks(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(
                    description = "<p>북마크 검색 종류. 목록은 다음과 같음</p>" +
                            "<ul>" +
                            "<li>ALL - 전체 조회</li>" +
                            "<li>WANT - 가보고 싶어요</li>" +
                            "<li>GONE - 다녀왔어요</li>" +
                            "</ul>",
                    example = "ALL"
            ) @RequestParam BookmarkSearchType searchType,
            @Parameter(
                    description = "페이지 번호 (0부터 시작합니다). 기본값은 0입니다.",
                    example = "0"
            ) @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(
                    description = "한 페이지에 담긴 데이터의 최대 개수(사이즈). 기본값은 20입니다.",
                    example = "20"
            ) @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(
                    description = "<p>북마크 검색 정렬 기준. 목록은 다음과 같음</p>" +
                            "<p>현재 인기순 정렬은 구현이 되지 않은 상태. 최신순 정렬로만 검색 가능</p>" +
                            "<ul>" +
                            "<li>LATEST - 최신순</li>" +
                            "<li>POPULARITY - 인기순</li>" +
                            "</ul>",
                    example = "ALL"
            )
            @RequestParam(required = false, defaultValue = "LATEST") BookmarkSearchSort sort
    ) {
        Slice<BookmarkResponse> response = bookmarkService.searchBookmarks(
                memberDetails.getId(),
                searchType,
                sort,
                PageRequest.of(page, size)
        ).map(BookmarkResponse::from);

        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "북마크 취소",
            description = "북마크를 취소합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200", content = @Content),
            @ApiResponse(description = "북마크 취소 권한이 없는 경우(내가 저장하지 않은 북마크를 삭제하려고 하는 경우).", responseCode = "403", content = @Content)
    })
    @DeleteMapping("/{bookmarkId}")
    public void delete(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long bookmarkId
    ) {
        bookmarkService.delete(memberDetails.getId(), bookmarkId);
    }
}
