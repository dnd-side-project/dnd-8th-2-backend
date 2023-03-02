package com.dnd.reetplace.unit.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.*;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import com.dnd.reetplace.app.service.BookmarkService;
import com.dnd.reetplace.app.type.BookmarkType;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkService sut;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;

    @DisplayName("회원 PK와 북마크 생성을 위한 정보가 주어지고 DB에 존재하는 장소에 대한 북마크를 생성하면 " +
            "북마크 저장 후 저장된 북마크 정보를 반환한다.")
    @Test
    void givenMemberIdAndBookmarkInfo_whenMarkingPlace_thenSaveAndReturnBookmark() {
        // given
        Long memberId = 1L;
        String kakaoPid = "test kakao pid";
        Member expectedMember = createMember();
        Place expectedFoundPlace = createPlace();
        Bookmark expectedSavedBookmark = createBookmark(expectedMember, expectedFoundPlace);
        given(memberRepository.findByIdAndDeletedAtIsNull(memberId)).willReturn(Optional.of(expectedMember));
        given(placeRepository.findByKakaoPid(kakaoPid)).willReturn(Optional.of(expectedFoundPlace));
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(expectedSavedBookmark);

        // when
        BookmarkDto actualSavedBookmark = sut.save(memberId, createNotSavedBookmarkDto());

        // then
        then(memberRepository).should().findByIdAndDeletedAtIsNull(memberId);
        then(placeRepository).should().findByKakaoPid(kakaoPid);
        then(placeRepository).shouldHaveNoMoreInteractions();
        then(bookmarkRepository).should().save(any(Bookmark.class));
        assertThat(actualSavedBookmark.getMember().getId()).isEqualTo(expectedMember.getId());
        assertThat(actualSavedBookmark.getPlace().getId()).isEqualTo(expectedFoundPlace.getId());
        assertThat(actualSavedBookmark.getId()).isEqualTo(expectedSavedBookmark.getId());
    }

    @DisplayName("회원 PK와 북마크 생성을 위한 정보가 주어지고 DB에 존재하지 않는 장소에 대해 북마크를 생성하면 " +
            "북마크 저장 후 저장된 북마크 정보를 반환한다.")
    @Test
    void givenMemberIdAndBookmarkInfo_whenMarkingNotExistentPlace_thenSaveBookmarkAndPlace() {
        // given
        Long memberId = 1L;
        String kakaoPid = "test kakao pid";
        Member expectedMember = createMember();
        Place expectedSavedPlace = createPlace();
        Bookmark expectedSavedBookmark = createBookmark(expectedMember, expectedSavedPlace);
        given(memberRepository.findByIdAndDeletedAtIsNull(memberId)).willReturn(Optional.of(expectedMember));
        given(placeRepository.findByKakaoPid(kakaoPid)).willReturn(Optional.empty());
        given(placeRepository.save(any(Place.class))).willReturn(expectedSavedPlace);
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(expectedSavedBookmark);

        // when
        BookmarkDto actualSavedBookmark = sut.save(memberId, createNotSavedBookmarkDto());

        // then
        then(memberRepository).should().findByIdAndDeletedAtIsNull(memberId);
        then(placeRepository).should().findByKakaoPid(kakaoPid);
        then(placeRepository).should().save(any(Place.class));
        then(bookmarkRepository).should().save(any(Bookmark.class));
        assertThat(actualSavedBookmark.getMember().getId()).isEqualTo(expectedMember.getId());
        assertThat(actualSavedBookmark.getPlace().getId()).isEqualTo(expectedSavedPlace.getId());
        assertThat(actualSavedBookmark.getId()).isEqualTo(expectedSavedBookmark.getId());
    }

    private Member createMember() {
        Member member = Member.builder()
                .uid("test")
                .loginType(LoginType.KAKAO)
                .nickname("test")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        return member;
    }

    private Place createPlace() {
        Place place = createNotSavedPlaceDto().toEntity();
        ReflectionTestUtils.setField(place, "id", 2L);

        return place;
    }

    private Bookmark createBookmark(Member member, Place place) {
        Bookmark bookmark = createNotSavedBookmarkDto().toEntity(member, place);
        ReflectionTestUtils.setField(bookmark, "id", 3L);

        return bookmark;
    }

    private BookmarkDto createNotSavedBookmarkDto() {
        return BookmarkDto.of(
                createNotSavedPlaceDto(),
                BookmarkType.WANT,
                (short) 2,
                "test",
                null
        );
    }

    private PlaceDto createNotSavedPlaceDto() {
        return PlaceDto.of(
                "test kakao pid",
                "test name",
                "test url",
                "test category1 > test category2 > test category3",
                PlaceCategoryGroupCode.CT1,
                PlaceCategory.CULTURE,
                PlaceSubCategory.CULTURE_CINEMA,
                "010-1234-5678",
                new Address(
                        "sido sgg detail",
                        "sido sgg road_detail"
                ),
                new Point("1.23", "4.56")
        );
    }
}