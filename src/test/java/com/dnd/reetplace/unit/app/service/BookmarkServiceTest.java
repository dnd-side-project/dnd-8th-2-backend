package com.dnd.reetplace.unit.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.*;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkUpdateRequest;
import com.dnd.reetplace.app.dto.bookmark.response.NumOfBookmarksResponse;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import com.dnd.reetplace.app.service.BookmarkService;
import com.dnd.reetplace.app.type.*;
import com.dnd.reetplace.global.exception.bookmark.AlreadyMarkedPlaceException;
import com.dnd.reetplace.global.exception.bookmark.BookmarkDeletePermissionDeniedException;
import com.dnd.reetplace.global.exception.bookmark.BookmarkUpdatePermissionDeniedException;
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
import static org.mockito.BDDMockito.*;

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

    @DisplayName("회원의 PK가 주어지고, 북마크 개수를 조회하면, 조회된 북마크 개수를 반환한다.")
    @Test
    void givenMemberId_whenGettingNumOfBookmarks_thenReturnNumOfBookmarks() {
        // given
        long memberId = 1L;
        Member dummyMember = createMember();
        Place dummyPlace = createPlace();
        List<Bookmark> bookmarks = List.of(createBookmark(dummyMember, dummyPlace),
                createBookmark(dummyMember, dummyPlace),
                createBookmark(dummyMember, dummyPlace));
        int numOfBookmarks = bookmarks.size();
        given(bookmarkRepository.findAllByMember(memberId))
                .willReturn(bookmarks);

        // when
        NumOfBookmarksResponse numOfBookmarksResponse = sut.getNumOfBookmarks(memberId);

        // then
        then(bookmarkRepository).should().findAllByMember(memberId);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(numOfBookmarksResponse.getNumOfAll()).isEqualTo(numOfBookmarks);
        assertThat(numOfBookmarksResponse.getNumOfWant()).isEqualTo(numOfBookmarks);
        assertThat(numOfBookmarksResponse.getNumOfDone()).isEqualTo(0);
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

    @DisplayName("수정할 북마크 정보가 주어지고, 북마크를 수정하면, 북마크 수정 후 수정된 북마크 정보가 반환된다.")
    @Test
    void givenBookmarkInfoToUpdate_whenUpdating_thenReturnUpdatedBookmark() {
        // given
        long memberId = 1L;
        long bookmarkId = 2L;
        Member member = createMember(memberId);
        Place place = createPlace();
        Bookmark bookmark = createBookmark(bookmarkId, member, place);
        BookmarkUpdateRequest infoToUpdate = new BookmarkUpdateRequest(BookmarkType.DONE, (short) 3, "update", "updateLink1", null, null);
        given(bookmarkRepository.findById(bookmarkId)).willReturn(Optional.of(bookmark));

        // when
        BookmarkDto updatedBookmark = sut.update(memberId, bookmarkId, infoToUpdate);

        // then
        then(bookmarkRepository).should().findById(bookmarkId);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(updatedBookmark.getType()).isEqualTo(infoToUpdate.getType());
        assertThat(updatedBookmark.getRate()).isEqualTo(infoToUpdate.getRate());
        assertThat(updatedBookmark.getPeople()).isEqualTo(infoToUpdate.getPeople());
        assertThat(updatedBookmark.getRelLinks().getRelLink1()).isEqualTo(infoToUpdate.getRelLink1());
        assertThat(updatedBookmark.getRelLinks().getRelLink2()).isEqualTo(infoToUpdate.getRelLink2());
        assertThat(updatedBookmark.getRelLinks().getRelLink3()).isEqualTo(infoToUpdate.getRelLink3());
    }

    @DisplayName("수정할 북마크 정보와 북마크를 소유하고 있지 않은 잘못된 회원의 PK가 주어지고, 북마크를 수정하려고 하면, 예외가 발생한다.")
    @Test
    void givenBookmarkInfoToUpdateAndInvalidMemberId_whenUpdating_thenThrowException() {
        // given
        long memberId = 1L;
        long bookmarkId = 2L;
        Member member = createMember(memberId);
        Place place = createPlace();
        Bookmark bookmark = createBookmark(bookmarkId, member, place);
        BookmarkUpdateRequest infoToUpdate = new BookmarkUpdateRequest(BookmarkType.DONE, (short) 3, "update", "updateLink1", null, null);
        given(bookmarkRepository.findById(bookmarkId)).willReturn(Optional.of(bookmark));

        // when
        Throwable t = catchThrowable(() -> sut.update(100L, bookmarkId, infoToUpdate));

        // then
        then(bookmarkRepository).should().findById(bookmarkId);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(BookmarkUpdatePermissionDeniedException.class);
    }

    @DisplayName("북마크와 북마크한 회원의 PK가 주어지고, 삭제하면, 북마크가 삭제된다.")
    @Test
    void givenBookmarkAndMemberId_whenDeleting_thenDeleteBookmark() {
        // given
        long memberId = 1L;
        long bookmarkId = 2L;
        Bookmark bookmark = createBookmark(createMember(memberId), createPlace());
        given(bookmarkRepository.findById(bookmarkId)).willReturn(Optional.of(bookmark));
        willDoNothing().given(bookmarkRepository).delete(bookmark);

        // when
        sut.delete(memberId, bookmarkId);

        // then
        then(bookmarkRepository).should().findById(bookmarkId);
        then(bookmarkRepository).should().delete(bookmark);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("북마크의 PK와 별개의 회원의 PK가 주어지고, 북마크를 삭제하면, 예외가 발생한다.")
    @Test
    void givenBookmarkAndAnotherMemberId_whenDeleting_thenThrowException() {
        // given
        long memberId = 1L;
        long anotherMemberId = 2L;
        long bookmarkId = 3L;
        Member member = createMember(memberId);
        Bookmark bookmark = createBookmark(member, createPlace());
        given(bookmarkRepository.findById(bookmarkId)).willReturn(Optional.of(bookmark));

        // when
        Throwable t = catchThrowable(() -> sut.delete(anotherMemberId, bookmarkId));

        // then
        then(bookmarkRepository).should().findById(bookmarkId);
        then(bookmarkRepository).shouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(BookmarkDeletePermissionDeniedException.class);
    }

    private Member createMember(Long memberId) {
        Member member = Member.builder()
                .uid("test")
                .loginType(LoginType.KAKAO)
                .nickname("test")
                .build();
        ReflectionTestUtils.setField(member, "id", memberId);

        return member;
    }

    private Member createMember() {
        return createMember(1L);
    }

    private Place createPlace() {
        Place place = createNotSavedPlaceDto().toEntity();
        ReflectionTestUtils.setField(place, "id", 2L);

        return place;
    }

    private Bookmark createBookmark(Member member, Place place) {
        return createBookmark(3L, member, place);
    }

    private Bookmark createBookmark(long bookmarkId, Member member, Place place) {
        Bookmark bookmark = createNotSavedBookmarkDto().toEntity(member, place);
        ReflectionTestUtils.setField(bookmark, "id", bookmarkId);
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