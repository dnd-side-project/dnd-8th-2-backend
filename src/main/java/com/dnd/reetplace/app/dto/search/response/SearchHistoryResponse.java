package com.dnd.reetplace.app.dto.search.response;

import com.dnd.reetplace.app.dto.search.SearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SearchHistoryResponse {

    @Schema(description = "검색기록 id", example = "1")
    private Long id;

    @Schema(description = "검색 키워드", example = "어글리 베이커리")
    private String query;

    @Schema(description = "검색 일시", example = "2023-11-12T10:15:30")
    private LocalDateTime createdAt;

    public static SearchHistoryResponse of(SearchDto search) {
        return new SearchHistoryResponse(search.getId(), search.getQuery(), search.getCreatedAt());
    }
}
