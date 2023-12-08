package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.dto.search.SearchDto;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.SearchRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
import com.dnd.reetplace.global.exception.search.SearchDeletePermissionDeniedException;
import com.dnd.reetplace.global.exception.search.SearchIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final MemberRepository memberRepository;

    /**
     * memberId에 해당하는 로그인 사용자의 검색 기록을 조회한다.
     *
     * @param memberId
     * @return
     */
    public SearchHistoryListResponse getSearchHistory(Long memberId) {
        List<SearchDto> searchDtoList = searchRepository.findByMemberIdAndDeletedAtIsNull(memberId)
                .stream()
                .map(SearchDto::from)
                .toList();
        return SearchHistoryListResponse.of(searchDtoList);
    }

    public void deleteSearchHistory(Long memberId, Long searchId) {
        // 사용자 유효성 검사
        this.validateLoginMember(memberId);
        // 검색기록 조회
        Search search = this.getSearch(searchId);
        // 검색기록 삭제 권한 확인
        this.validateSearchDeletePermission(memberId, search);
        // 검색기록 삭제
        searchRepository.delete(search);
    }

    public void deleteAllSearchHistory(Long memberId) {
        // 사용자 유효성 검사
        this.validateLoginMember(memberId);
        // 검색기록 삭제
        searchRepository.deleteAllByMemberId(memberId);
    }

    private Search getSearch(Long searchId) {
        return searchRepository.findByIdAndDeletedAtIsNull(searchId)
                .orElseThrow(() -> new SearchIdNotFoundException(searchId));
    }

    private void validateSearchDeletePermission(Long memberId, Search search) {
        if (!search.getMember().getId().equals(memberId)) {
            throw new SearchDeletePermissionDeniedException();
        }
    }

    /**
     * memberId를 통해 사용자 유효성을 검사한다.
     *
     * @param memberId 사용자 id
     */
    private void validateLoginMember(Long memberId) {
        memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
    }
}
