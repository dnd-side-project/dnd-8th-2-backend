package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.place.Place;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final MemberService memberService;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 특정 장소에 대한 북마크를 생성한다.
     *
     * @param memberId 북마크를 하고자 하는 회원의 PK
     * @param request 북마크 생성을 위해 필요한 정보. 장소에 대한 정보도 포함되어 있음
     * @return 생성된 북마크 정보
     */
    @Transactional
    public BookmarkDto save(Long memberId, BookmarkDto bookmarkDto) {
        Member member = memberService.getMember(memberId);

        PlaceDto placeDto = bookmarkDto.getPlace();
        Place place = placeRepository.findByKakaoPid(placeDto.getKakaoPid())
                .orElseGet(() -> placeRepository.save(placeDto.toEntity()));

        return BookmarkDto.from(
                bookmarkRepository.save(bookmarkDto.toEntity(member, place))
        );
    }
}