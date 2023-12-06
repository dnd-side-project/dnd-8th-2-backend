package com.dnd.reetplace.unit.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.repository.SearchRepository;
import com.dnd.reetplace.app.service.SearchService;
import com.dnd.reetplace.app.type.LoginType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    private SearchService sut;

    @Mock
    private SearchRepository searchRepository;

    @DisplayName("검색기록 조회에 성공한다.")
    @Test
    void givenLoginUserSearchHistory_whenGetSearchHistory_thenReturnLoginUserSearchHistory() {
        // given
        Member member = createMember(1L);
        Search search1 = Search.builder().query("test1").member(member).build();
        Search search2 = Search.builder().query("test1").member(member).build();
        Search search3 = Search.builder().query("test1").member(member).build();
        List<Search> searchList = List.of(search1, search2, search3);
        given(searchRepository.findByMemberIdAndDeletedAtIsNull(member.getId()))
                .willReturn(List.of(search1, search2, search3));

        // when
        SearchHistoryListResponse result = sut.getSearchHistory(member.getId());

        // then
        assertThat(result.getContents().size()).isEqualTo(searchList.size());
        assertThat(result.getContents().get(0).getQuery()).isEqualTo(searchList.get(0).getQuery());
        assertThat(result.getContents().get(1).getQuery()).isEqualTo(searchList.get(1).getQuery());
        assertThat(result.getContents().get(2).getQuery()).isEqualTo(searchList.get(2).getQuery());
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
}