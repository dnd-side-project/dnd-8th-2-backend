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

    @Schema(description = """
            <p>북마크 종류. 목록은 다음과 같음</p>
            <ul>
                <li>WANT - 가보고 싶어요</li>
                <li>GONE - 다녀왔어요</li>
            </ul>
            """,
            example = "WANT")
    private BookmarkType type;

    public static BookmarkSummaryResponse from(BookmarkDto bookmarkDto) {
        return new BookmarkSummaryResponse(
                bookmarkDto.getId(),
                PlaceSummaryResponse.from(bookmarkDto.getPlace()),
                bookmarkDto.getType()
        );
    }
}
