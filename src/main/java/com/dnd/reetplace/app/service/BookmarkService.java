package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkUpdateRequest;
import com.dnd.reetplace.app.dto.bookmark.response.NumOfBookmarksResponse;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import com.dnd.reetplace.app.type.BookmarkSearchSort;
import com.dnd.reetplace.app.type.BookmarkSearchType;
import com.dnd.reetplace.app.type.BookmarkType;
import com.dnd.reetplace.global.exception.bookmark.AlreadyMarkedPlaceException;
import com.dnd.reetplace.global.exception.bookmark.BookmarkDeletePermissionDeniedException;
import com.dnd.reetplace.global.exception.bookmark.BookmarkNotFoundByIdException;
import com.dnd.reetplace.global.exception.bookmark.BookmarkUpdatePermissionDeniedException;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dnd.reetplace.app.type.BookmarkSearchType.ALL;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 특정 장소에 대한 북마크를 생성한다.
     *
     * @param memberId    북마크를 하고자 하는 회원의 PK
     * @param bookmarkDto 북마크 생성을 위해 필요한 정보. 장소에 대한 정보도 포함되어 있음
     * @return 생성된 북마크 정보
     */
    @Transactional
    public BookmarkDto save(Long memberId, BookmarkDto bookmarkDto) {
        validateAlreadyMarkedPlace(memberId, bookmarkDto.getPlace());

        Member member = getExistentMember(memberId);

        PlaceDto placeDto = bookmarkDto.getPlace();
        Place place = placeRepository.findByKakaoPid(placeDto.getKakaoPid())
                .orElseGet(() -> placeRepository.save(placeDto.toEntity()));

        return BookmarkDto.from(
                bookmarkRepository.save(bookmarkDto.toEntity(member, place))
        );
    }

    /**
     * 북마크 개수를 조회한다.
     *
     * @param loginMemberId 북마크 개수를 조회하고자 하는 로그인한 회원
     * @return 전체, "가보고싶어요", "다녀왔어요"로 표시한 북마크의 개수가 응답된다.
     */
    public NumOfBookmarksResponse getNumOfBookmarks(Long loginMemberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(loginMemberId);

        int numOfAll = bookmarks.size();
        int numOfWant = (int) bookmarks.stream()
                .filter(bookmark -> bookmark.getType() == BookmarkType.WANT)
                .count();
        int numOfDone = numOfAll - numOfWant;

        return new NumOfBookmarksResponse(numOfAll, numOfWant, numOfDone);
    }

    /**
     * 북마크 리스트를 검색한다.
     *
     * @param memberId   북마크 리스트를 검색하고자 하는 로그인 회원
     * @param searchType 검색하고자 하는 북마크 리스트의 종류 (전체, 가고 싶어요, 갔다 왔어요)
     * @param sort       정렬 기준 (최신순, 인기순)
     * @param pageable   paging 정보
     * @return 북마크 정보가 담긴 {@link Slice} 객체
     */
    public Slice<BookmarkDto> searchBookmarks(Long memberId, BookmarkSearchType searchType, BookmarkSearchSort sort, Pageable pageable) {
        if (searchType.equals(ALL)) {
            return bookmarkRepository
                    .findByMember_IdOrderByCreatedAtDesc(memberId, pageable)
                    .map(BookmarkDto::from);
        } else {
            return bookmarkRepository
                    .findByTypeAndMember_IdOrderByCreatedAtDesc(searchType.toBookmarkType(), memberId, pageable)
                    .map(BookmarkDto::from);
        }
    }

    /**
     * 북마크 정보를 수정한다.
     *
     * @param loginMemberId 북마크를 수정하고자 하는 로그인 회원의 PK
     * @param bookmarkId    수정하고자 하는 북마크의 PK
     * @param updateRequest 수정하려는 정보
     * @return 수정된 북마크 정보가 담긴 dto
     */
    @Transactional
    public BookmarkDto update(Long loginMemberId, Long bookmarkId, BookmarkUpdateRequest updateRequest) {
        Bookmark bookmark = findById(bookmarkId);
        validateBookmarkUpdatePermission(loginMemberId, bookmark);
        bookmark.update(updateRequest);
        return BookmarkDto.from(bookmark);
    }

    /**
     * 북마크 저장을 취소(삭제)한다.
     *
     * @param loginMemberId 북마크를 삭제하고자 하는 회원(로그인 회원)의 PK.
     * @param bookmarkId    삭제하고자 하는 북마크
     */
    @Transactional
    public void delete(Long loginMemberId, Long bookmarkId) {
        Bookmark bookmark = this.findById(bookmarkId);
        validateBookmarkDeletePermission(loginMemberId, bookmark);
        bookmarkRepository.delete(bookmark);
    }

    private Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundByIdException(bookmarkId));
    }

    /**
     * memberId에 해당하는 사용자를 반환한다.
     * memberId에 해당하는 사용자가 존재하지 않거나 탈퇴한 사용자일 시, Exception을 던진다.
     *
     * @param memberId 찾고자 하는 사용자의 id
     * @return id에 해당하는 Member Entity
     */
    private Member getExistentMember(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
    }

    /**
     * 이미 저장한 장소인지 검증한다.
     *
     * @param memberId PK of login member
     * @param place    저장 여부를 확인할 장소
     */
    private void validateAlreadyMarkedPlace(Long memberId, PlaceDto place) {
        String kakaoPid = place.getKakaoPid();
        if (bookmarkRepository.existsByMember_IdAndPlace_KakaoPid(memberId, kakaoPid)) {
            throw new AlreadyMarkedPlaceException(memberId, kakaoPid);
        }
    }
    /**
     * 북마크 수정 권한을 확인한다.
     * 북마크를 생성한 회원(소유자)만이 수정이 가능하다.
     *
     * @param loginMemberId 북마크를 수정하고자 하는 회원(로그인 회원)의 PK.
     * @param bookmark      수정하고자 하는 북마크
     */
    private void validateBookmarkUpdatePermission(Long loginMemberId, Bookmark bookmark) {
        if (!bookmark.getMember().getId().equals(loginMemberId)) {
            throw new BookmarkUpdatePermissionDeniedException();
        }
    }

    /**
     * 북마크 삭제 권한을 확인한다.
     * 북마크를 생성한 회원(소유자)만이 삭제가 가능하다.
     *
     * @param loginMemberId 북마크를 삭제하고자 하는 회원(로그인 회원)의 PK.
     * @param bookmark      삭제하고자 하는 북마크
     */
    private void validateBookmarkDeletePermission(Long loginMemberId, Bookmark bookmark) {
        if (!bookmark.getMember().getId().equals(loginMemberId)) {
            throw new BookmarkDeletePermissionDeniedException();
        }
    }
}
