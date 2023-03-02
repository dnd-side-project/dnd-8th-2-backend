package com.dnd.reetplace.app.controller;

import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkCreateRequest;
import com.dnd.reetplace.app.dto.bookmark.response.BookmarkCreateResponse;
import com.dnd.reetplace.app.service.BookmarkService;
import com.dnd.reetplace.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@Tag(name = "장소 북마크", description = "장소 북마크 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping
    public ResponseEntity<BookmarkCreateResponse> save(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody BookmarkCreateRequest request
    ) {
        BookmarkDto bookmarkDto = bookmarkService.save(memberDetails.getId(), request.toDto());

        return ResponseEntity
                .created(URI.create("/api/bookmarks/" + bookmarkDto.getId()))
                .body(BookmarkCreateResponse.from(bookmarkDto));
    }
}
