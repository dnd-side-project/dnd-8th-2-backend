package com.dnd.reetplace.app.domain.bookmark;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkUpdateRequest;
import com.dnd.reetplace.app.type.BookmarkType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "place_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Place place;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookmarkType type;

    private String thumbnailUrl;

    @Column(nullable = false)
    private Short rate;

    private String people;

    @Embedded
    private BookMarkRelLink relLinks;

    @Builder
    private Bookmark(
            Member member,
            Place place,
            BookmarkType type,
            String thumbnailUrl,
            Short rate,
            String people,
            BookMarkRelLink relLinks
    ) {
        this.member = member;
        this.place = place;
        this.type = type;
        this.thumbnailUrl = thumbnailUrl;
        this.rate = rate;
        this.people = people;
        this.relLinks = relLinks;
    }

    public void update(BookmarkUpdateRequest updateInfo) {
        this.type = updateInfo.getType();
        this.rate = updateInfo.getRate();
        this.people = updateInfo.getPeople();
        this.relLinks = new BookMarkRelLink(updateInfo.getRelLink1(), updateInfo.getRelLink2(), updateInfo.getRelLink3());
    }
}
