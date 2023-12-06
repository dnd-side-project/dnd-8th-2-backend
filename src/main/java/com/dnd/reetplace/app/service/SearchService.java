package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.dto.search.SearchDto;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchService {

    private final SearchRepository searchRepository;

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
}
