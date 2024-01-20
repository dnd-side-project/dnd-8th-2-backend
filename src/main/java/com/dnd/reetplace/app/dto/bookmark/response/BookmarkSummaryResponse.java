package com.dnd.reetplace.app.dto.bookmark.response;

import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.place.response.PlaceSummaryResponse;
import com.dnd.reetplace.app.type.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkSummaryResponse {

    @Schema(description = "생성된 북마크 PK", example = "1")
    private Long id;

    @Schema(description = "북마크 한 장소 정보")
    private PlaceSummaryResponse place;

    @Schema(description = "북마크 종류")
    private BookmarkType type;

    public static BookmarkSummaryResponse from(BookmarkDto bookmarkDto) {
        return new BookmarkSummaryResponse(
                bookmarkDto.getId(),
                PlaceSummaryResponse.from(bookmarkDto.getPlace()),
                bookmarkDto.getType()
        );
    }
}
