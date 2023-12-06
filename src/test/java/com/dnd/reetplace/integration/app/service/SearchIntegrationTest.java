package com.dnd.reetplace.integration.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.SearchRepository;
import com.dnd.reetplace.app.service.PlaceService;
import com.dnd.reetplace.app.service.SearchService;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class SearchIntegrationTest {

    @Autowired
    SearchService searchService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    TokenProvider tokenProvider;

    @DisplayName("검색기록 조회에 성공한다.")
    @Test
    void givenLoginUserSearchHistory_whenGetSearchHistory_thenReturnLoginUserSearchHistory() {

        // given
        Member savedMember = memberRepository.save(createMember(1L));
        login(savedMember);
        for (int i = 0; i < 7; i++) {
            int searchHistoryCount = searchRepository.countByMemberId(savedMember.getId());
            // 검색기록 5개 초과 시 가장 오래된 검색기록 삭제
            if (searchHistoryCount >= 5) {
                searchRepository.deleteLatestSearchByMemberId(savedMember.getId());
            }
            // 검색기록 저장
            Search search = Search.builder()
                    .member(savedMember)
                    .query("test" + i)
                    .build();
            searchRepository.save(search);
        }

        // when
        SearchHistoryListResponse result = searchService.getSearchHistory(savedMember.getId());
        List<Search> all = searchRepository.findAll();


        // then
        assertThat(result.getContents().size()).isEqualTo(5);
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test0"))).isFalse();
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test1"))).isFalse();
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test2"))).isTrue();
    }

    private void login(Member member) {
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        Authentication authToken = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
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
