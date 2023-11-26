package com.dnd.reetplace.app.dto.search.response;

import com.dnd.reetplace.app.dto.search.SearchDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SearchHistoryListResponse {
    private List<SearchHistoryResponse> contents;

    public static SearchHistoryListResponse of(List<SearchDto> contents) {
        return new SearchHistoryListResponse(contents.stream().map(SearchHistoryResponse::of).toList());
    }
}
