package com.dnd.reetplace.app.dto.bookmark.request;

import com.dnd.reetplace.app.type.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class BookmarkUpdateRequest {

    @Schema(description = "북마크 종류")
    @NotNull
    private BookmarkType type;

    @Schema(description = "별점. 개수로 표현함 (1, 2, 3 중 하나)", example = "2")
    @NotNull
    @Min(1)
    @Max(3)
    private Short rate;

    @Schema(description = "함께하는 사람들", example = "박신영, 최나은, 김희재, 김태현, 박훈성")
    private String people;

    @Schema(description = "장소와 관련된 URL1", example = "https://redbutton.co.kr/")
    private String relLink1;

    @Schema(description = "장소와 관련된 URL2", example = "https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=jetpy&logNo=221706849387")
    private String relLink2;

    @Schema(description = "장소와 관련된 URL3", example = "null")
    private String relLink3;
}
