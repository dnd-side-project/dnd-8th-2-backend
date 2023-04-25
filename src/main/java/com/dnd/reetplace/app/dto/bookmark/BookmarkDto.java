package com.dnd.reetplace.app.dto.bookmark;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.BookMarkRelLink;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.type.BookmarkType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkDto {

    private Long id;
    private MemberDto member;
    private PlaceDto place;
    private BookmarkType type;
    private String thumbnailUrl;
    private Short rate;
    private String people;
    private BookMarkRelLink relLinks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookmarkDto of(PlaceDto place, BookmarkType type, String thumbnailUrl, Short rate, String people, BookMarkRelLink relLinks) {
        return of(null, null, place, type, thumbnailUrl, rate, people, relLinks, null, null);
    }

    public static BookmarkDto of(Long id, MemberDto member, PlaceDto place, BookmarkType type, String thumbnailUrl, Short rate, String people, BookMarkRelLink relLinks, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new BookmarkDto(id, member, place, type, thumbnailUrl, rate, people, relLinks, createdAt, updatedAt);
    }

    public static BookmarkDto from(Bookmark entity) {
        return of(
                entity.getId(),
                MemberDto.from(entity.getMember()),
                PlaceDto.from(entity.getPlace()),
                entity.getType(),
                entity.getThumbnailUrl(),
                entity.getRate(),
                entity.getPeople(),
                entity.getRelLinks(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Bookmark toEntity(Member member, Place place) {
        return Bookmark.builder()
                .member(member)
                .place(place)
                .type(type)
                .thumbnailUrl(thumbnailUrl)
                .rate(rate)
                .people(people)
                .relLinks(relLinks)
                .build();
    }
}
