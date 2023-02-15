package com.dnd.reetplace.app.domain.bookmark;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.domain.place.Place;
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
            Short rate,
            String people,
            String relLink1,
            String relLink2,
            String relLink3
    ) {
        this.member = member;
        this.place = place;
        this.type = type;
        this.rate = rate;
        this.people = people;
        this.relLinks = new BookMarkRelLink(relLink1, relLink2, relLink3);
    }
}
