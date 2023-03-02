package com.dnd.reetplace.app.dto.bookmark.request;

import com.dnd.reetplace.app.domain.bookmark.BookMarkRelLink;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.place.request.PlaceRequest;
import com.dnd.reetplace.app.type.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class BookmarkCreateRequest {

    @Schema(description = "장소 정보")
    private PlaceRequest place;

    @Schema(description = "<p>북마크 종류. 목록은 다음과 같음</p>" +
            "<ul>" +
            "<li>WANT - 가보고 싶어요</li>" +
            "<li>GONE - 다녀왔어요</li>" +
            "</ul>",
            example = "WANT")
    @NotNull
    private BookmarkType type;

    @Schema(description = "별점. 개수로 표현함 (1, 2, 3 중 하나)", example = "2")
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

    public BookmarkDto toDto() {
        return BookmarkDto.of(
                this.getType(),
                this.getRate(),
                this.getPeople(),
                new BookMarkRelLink(this.getRelLink1(), this.getRelLink2(), this.getRelLink3())
        );
    }
}
