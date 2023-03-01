package com.dnd.reetplace.app.dto.bookmark.response;

import com.dnd.reetplace.app.domain.bookmark.BookMarkRelLink;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.member.response.MemberResponse;
import com.dnd.reetplace.app.dto.place.response.PlaceResponse;
import com.dnd.reetplace.app.type.BookmarkType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkCreateResponse {

    private Long id;

    private MemberResponse member;

    private PlaceResponse place;

    private BookmarkType type;

    private Short rate;

    private String people;

    private BookMarkRelLink relLinks;

    public static BookmarkCreateResponse from(BookmarkDto bookmarkDto) {
        return new BookmarkCreateResponse(
                bookmarkDto.getId(),
                MemberResponse.from(bookmarkDto.getMember()),
                PlaceResponse.from(bookmarkDto.getPlace()),
                bookmarkDto.getType(),
                bookmarkDto.getRate(),
                bookmarkDto.getPeople(),
                bookmarkDto.getRelLinks()
        );
    }
}
