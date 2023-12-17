package com.dnd.reetplace.app.dto.search;

import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SearchDto {

    private MemberDto member;
    private Long id;
    private String query;
    private LocalDateTime createdAt;

    public static SearchDto from(Search entity) {
        return of(MemberDto.from(entity.getMember()), entity.getId(), entity.getQuery(), entity.getCreatedAt());
    }

    public static SearchDto of(MemberDto member, Long id, String query, LocalDateTime createdAt) {
        return new SearchDto(member, id, query, createdAt);
    }
}
