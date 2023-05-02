package com.dnd.reetplace.app.dto.bookmark.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NumOfBookmarksResponse {

    @Schema(description = "전체 북마크 개수", example = "15")
    private Integer numOfAll;

    @Schema(description = "\"가고싶어요\"로 표시한 북마크 개수", example = "10")
    private Integer numOfWant;

    @Schema(description = "\"다녀왔어요\"로 표시한 북마크 개수", example = "5")
    private Integer numOfDone;
}
