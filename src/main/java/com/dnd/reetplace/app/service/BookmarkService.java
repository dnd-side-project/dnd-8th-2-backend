package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param memberId 북마크를 하고자 하는 회원의 PK
     * @param bookmarkDto 북마크 생성을 위해 필요한 정보. 장소에 대한 정보도 포함되어 있음
     * @return 생성된 북마크 정보
     */
    @Transactional
    public BookmarkDto save(Long memberId, BookmarkDto bookmarkDto) {
        Member member = getExistentMember(memberId);

        PlaceDto placeDto = bookmarkDto.getPlace();
        Place place = placeRepository.findByKakaoPid(placeDto.getKakaoPid())
                .orElseGet(() -> placeRepository.save(placeDto.toEntity()));

        return BookmarkDto.from(
                bookmarkRepository.save(bookmarkDto.toEntity(member, place))
        );
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
}
