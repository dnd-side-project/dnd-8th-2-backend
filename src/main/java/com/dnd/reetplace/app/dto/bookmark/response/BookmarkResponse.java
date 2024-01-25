package com.dnd.reetplace.app.dto.bookmark.response;

import com.dnd.reetplace.app.domain.bookmark.BookMarkRelLink;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.member.response.MemberResponse;
import com.dnd.reetplace.app.dto.place.response.PlaceResponse;
import com.dnd.reetplace.app.type.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkResponse {

    @Schema(description = "생성된 북마크 PK", example = "1")
    private Long id;

    @Schema(description = "북마크를 생성한 회원")
    private MemberResponse member;

    @Schema(description = "북마크 한 장소 정보")
    private PlaceResponse place;

    @Schema(description = "북마크 종류")
    private BookmarkType type;

    @Schema(description = "썸네일 이미지 url", example = "https://t1.daumcdn.net/place/75B5CF9ACCF84162A7E13CB1FD4D5D43")
    private String thumbnailImage;

    @Schema(description = "별점. 개수로 표현함 (1, 2, 3 중 하나)", example = "2")
    private Short rate;

    @Schema(description = "함께하는 사람들", example = "박신영, 최나은, 김희재, 김태현, 박훈성")
    private String people;

    @Schema(description = "장소와 관련된 URL1", example = "https://redbutton.co.kr/")
    private String relLink1;

    @Schema(description = "장소와 관련된 URL2", example = "https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=jetpy&logNo=221706849387")
    private String relLink2;

    @Schema(description = "장소와 관련된 URL3", example = "null")
    private String relLink3;

    public static BookmarkResponse from(BookmarkDto bookmarkDto) {
        BookMarkRelLink relLinks = bookmarkDto.getRelLinks();
        return new BookmarkResponse(
                bookmarkDto.getId(),
                MemberResponse.from(bookmarkDto.getMember()),
                PlaceResponse.from(bookmarkDto.getPlace()),
                bookmarkDto.getType(),
                bookmarkDto.getThumbnailUrl(),
                bookmarkDto.getRate(),
                bookmarkDto.getPeople(),
                relLinks != null ? relLinks.getRelLink1() : null,
                relLinks != null ? relLinks.getRelLink2() : null,
                relLinks != null ? relLinks.getRelLink3() : null
        );
    }
}
