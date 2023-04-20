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
import com.dnd.reetplace.app.type.*;
import com.dnd.reetplace.global.exception.bookmark.AlreadyMarkedPlaceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
        given(bookmarkRepository.existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid)).willReturn(false);
        given(memberRepository.findByIdAndDeletedAtIsNull(memberId)).willReturn(Optional.of(expectedMember));
        given(placeRepository.findByKakaoPid(kakaoPid)).willReturn(Optional.of(expectedFoundPlace));
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(expectedSavedBookmark);

        // when
        BookmarkDto actualSavedBookmark = sut.save(memberId, createNotSavedBookmarkDto());

        // then
        then(bookmarkRepository).should().existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid);
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
        given(bookmarkRepository.existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid)).willReturn(false);
        given(memberRepository.findByIdAndDeletedAtIsNull(memberId)).willReturn(Optional.of(expectedMember));
        given(placeRepository.findByKakaoPid(kakaoPid)).willReturn(Optional.empty());
        given(placeRepository.save(any(Place.class))).willReturn(expectedSavedPlace);
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(expectedSavedBookmark);

        // when
        BookmarkDto actualSavedBookmark = sut.save(memberId, createNotSavedBookmarkDto());

        // then
        then(bookmarkRepository).should().existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid);
        then(memberRepository).should().findByIdAndDeletedAtIsNull(memberId);
        then(placeRepository).should().findByKakaoPid(kakaoPid);
        then(placeRepository).should().save(any(Place.class));
        then(bookmarkRepository).should().save(any(Bookmark.class));
        assertThat(actualSavedBookmark.getMember().getId()).isEqualTo(expectedMember.getId());
        assertThat(actualSavedBookmark.getPlace().getId()).isEqualTo(expectedSavedPlace.getId());
        assertThat(actualSavedBookmark.getId()).isEqualTo(expectedSavedBookmark.getId());
    }

    @DisplayName("북마크 정보가 주어지고, 이미 마킹했던 장소를 다시 북마크 하려고 하는 경우, 예외를 발생시킨다.")
    @Test
    void givenBookmarkInfo_whenMarkingAlreadyMarkedPlace_thenThrowException() {
        // given
        Long memberId = 1L;
        String kakaoPid = "test kakao pid";
        given(bookmarkRepository.existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid)).willReturn(true);

        // when
        Throwable t = catchThrowable(() -> sut.save(memberId, createNotSavedBookmarkDto()));

        // then
        then(bookmarkRepository).should().existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(AlreadyMarkedPlaceException.class);
    }

    @DisplayName("북마크 전체 검색을 하면, 북마크 slice를 반환한다.")
    @Test
    void givenSearchTypeAll_whenSearchingBookmarks_thenReturnBookmarkSlice() {
        // given
        Long memberId = 1L;
        BookmarkSearchSort sort = BookmarkSearchSort.LATEST;
        Pageable pageable = Pageable.ofSize(20);
        PageImpl<Bookmark> expectedBookmarks = new PageImpl<>(List.of(createBookmark(createMember(), createPlace())));
        given(bookmarkRepository.findByMember_IdOrderByCreatedAtDesc(memberId, pageable)).willReturn(expectedBookmarks);

        // when
        Slice<BookmarkDto> actualBookmarks = sut.searchBookmarks(memberId, BookmarkSearchType.ALL, sort, pageable);

        // then
        then(bookmarkRepository).should().findByMember_IdOrderByCreatedAtDesc(memberId, pageable);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(actualBookmarks.getContent().get(0).getId())
                .isEqualTo(expectedBookmarks.getContent().get(0).getId());
    }

    @DisplayName("북마크 검색 종류가 주어지고, 북마크 전체 검색을 하면, 북마크 slice를 반환한다.")
    @Test
    void givenSearchTypeNotAll_whenSearchingBookmarks_thenReturnBookmarkSlice() {
        // given
        Long memberId = 1L;
        BookmarkSearchType searchType = BookmarkSearchType.WANT;
        BookmarkSearchSort sort = BookmarkSearchSort.LATEST;
        Pageable pageable = Pageable.ofSize(20);
        PageImpl<Bookmark> expectedBookmarks = new PageImpl<>(List.of(createBookmark(createMember(), createPlace())));
        given(bookmarkRepository.findByTypeAndMember_IdOrderByCreatedAtDesc(searchType.toBookmarkType(), memberId, pageable))
                .willReturn(expectedBookmarks);

        // when
        Slice<BookmarkDto> actualBookmarks = sut.searchBookmarks(memberId, searchType, sort, pageable);

        // then
        then(bookmarkRepository).should().findByTypeAndMember_IdOrderByCreatedAtDesc(searchType.toBookmarkType(), memberId, pageable);
        assertThat(actualBookmarks.getContent().get(0).getId())
                .isEqualTo(expectedBookmarks.getContent().get(0).getId());
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
                "https://thumbnail-image-url",
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
                PlaceSubCategory.CINEMA,
                "010-1234-5678",
                new Address(
                        "sido sgg detail",
                        "sido sgg road_detail"
                ),
                new Point("1.23", "4.56")
        );
    }
}