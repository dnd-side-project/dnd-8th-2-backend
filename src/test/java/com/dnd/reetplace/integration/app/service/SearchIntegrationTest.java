package com.dnd.reetplace.integration.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.SearchRepository;
import com.dnd.reetplace.app.service.PlaceService;
import com.dnd.reetplace.app.service.SearchService;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import com.dnd.reetplace.global.exception.search.SearchDeletePermissionDeniedException;
import com.dnd.reetplace.global.exception.search.SearchIdNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class SearchIntegrationTest {

    @Autowired
    SearchService sut;

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
        SearchHistoryListResponse result = sut.getSearchHistory(savedMember.getId());
        List<Search> all = searchRepository.findAll();


        // then
        assertThat(result.getContents().size()).isEqualTo(5);
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test0"))).isFalse();
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test1"))).isFalse();
        assertThat(all.stream().map(Search::getQuery).anyMatch(q -> q.equals("test2"))).isTrue();
    }

    @DisplayName("검색기록 삭제(단건)에 성공한다.")
    @Test
    void whenDeleteSearchHistory_thenSuccess() {
        // given
        Member savedMember = memberRepository.save(createMember(1L));
        login(savedMember);
        Search savedSearch = Search.builder()
                .member(savedMember) // 검색기록 저장
                .query("test")
                .build();
        searchRepository.save(savedSearch);

        // when
        sut.deleteSearchHistory(savedMember.getId(), savedSearch.getId());

        // then
        assertThat(searchRepository.findByMemberIdAndDeletedAtIsNull(savedMember.getId())).isEmpty();
    }

    @DisplayName("검색기록 삭제 시 존재하지 않는 사용자면 실패한다.")
    @Test
    void whenDeleteSearchHistoryWithNotExistsMember_thenFail() {
        // given
        Member savedMember = memberRepository.save(createMember(1L));
        login(savedMember);
        Search savedSearch = Search.builder()
                .member(savedMember) // 검색기록 저장
                .query("test")
                .build();
        searchRepository.save(savedSearch);

        // when & then
        assertThrows(MemberIdNotFoundException.class,
                () -> sut.deleteSearchHistory(savedMember.getId() + 1L, savedSearch.getId())); // 존재하지 않는 사용자
    }

    @DisplayName("검색기록 삭제(단건) 시 존재하지 않는 검색기록이면 실패한다.")
    @Test
    void whenDeleteSearchHistoryWithNotExistsSearch_thenFail() {
        // given
        Member savedMember = memberRepository.save(createMember(1L));
        login(savedMember);
        Search savedSearch = Search.builder()
                .member(savedMember) // 검색기록 저장
                .query("test")
                .build();
        searchRepository.save(savedSearch);

        // when & then
        assertThrows(SearchIdNotFoundException.class,
                () -> sut.deleteSearchHistory(savedMember.getId(), savedSearch.getId() + 1)); // 존재하지 않는 검색기록
    }

    @DisplayName("검색기록 삭제(단건) 시 자신의 검색기록이 아니면 실패한다.")
    @Test
    void whenDeleteSearchHistoryWithNotSearchHistoryOwner_thenFail() {
        // given
        Member savedMember1 = memberRepository.save(createMember(1L));
        Member savedMember2 = memberRepository.save(createMember(2L));
        login(savedMember1); // 로그인 (member 1)
        Search savedSearch = Search.builder()
                .member(savedMember2) // 검색기록 저장 (member 2)
                .query("test")
                .build();
        searchRepository.save(savedSearch);

        // when & then
        assertThrows(SearchDeletePermissionDeniedException.class,
                () -> sut.deleteSearchHistory(savedMember1.getId(), savedSearch.getId())); // 타 사용자의 검색기록 삭제 요청
    }

    @DisplayName("검색기록 삭제(전체)에 성공한다.")
    @Test
    void whenDeleteAllSearchHistory_thenSuccess() {
        // given
        Member savedMember = memberRepository.save(createMember(1L));
        login(savedMember);
        for (int i = 0; i < 5; i++) {
            // 검색기록 저장
            Search search = Search.builder()
                    .member(savedMember)
                    .query("test" + i)
                    .build();
            searchRepository.save(search);
        }

        // when
        sut.deleteAllSearchHistory(savedMember.getId());

        // then
        assertThat(searchRepository.findByMemberIdAndDeletedAtIsNull(savedMember.getId())).isEmpty();
    }

    private void login(Member member) {
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        Authentication authToken = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private Member createMember(Long memberId) {
        Member member = Member.builder()
                .uid("test" + memberId)
                .loginType(LoginType.KAKAO)
                .nickname("test" + memberId)
                .build();
        ReflectionTestUtils.setField(member, "id", memberId);

        return member;
    }
}
