package com.dnd.reetplace.unit.app.controller;

import com.dnd.reetplace.app.config.SecurityConfig;
import com.dnd.reetplace.app.controller.SearchController;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.search.SearchDto;
import com.dnd.reetplace.app.dto.search.response.SearchHistoryListResponse;
import com.dnd.reetplace.app.service.SearchService;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.security.MemberDetails;
import com.dnd.reetplace.global.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = SearchController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        })
public class SearchControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private SearchService searchService;

    @MockBean
    private TokenProvider tokenProvider;

    @DisplayName("검색기록 조회에 성공한다.")
    @Test
    void givenLoginUserSearchHistory_whenGetSearchHistory_thenReturnLoginUserSearchHistory() throws Exception {
        // given
        MemberDetails memberDetails = new MemberDetails(createMockMember(1L));
        ReflectionTestUtils.setField(memberDetails.getMember(), "id", 1L);
        MemberDto memberDto = MemberDto.of(
                memberDetails.getId(),
                memberDetails.getUid(),
                memberDetails.getLoginType(),
                memberDetails.getMember().getNickname(),
                memberDetails.getMember().getEmail()
        );
        SearchDto search1 = SearchDto.of(memberDto, "test1", LocalDateTime.now());
        SearchDto search2 = SearchDto.of(memberDto, "test2", LocalDateTime.now());
        SearchDto search3 = SearchDto.of(memberDto, "test3", LocalDateTime.now());
        SearchHistoryListResponse response = SearchHistoryListResponse.of(List.of(search1, search2, search3));
        given(searchService.getSearchHistory(any()))
                .willReturn(response);

        // when & then
        mvc.perform(
                        get("/api/search/history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .with(user(memberDetails))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()").value(response.getContents().size()))
                .andExpect(jsonPath("$.contents[0].query").value(response.getContents().get(0).getQuery()))
                .andExpect(jsonPath("$.contents[1].query").value(response.getContents().get(1).getQuery()))
                .andExpect(jsonPath("$.contents[2].query").value(response.getContents().get(2).getQuery()));
    }

    private Member createMockMember(Long id) {
        return Member.builder()
                .uid("testUid" + id)
                .loginType(LoginType.KAKAO)
                .nickname("test" + id)
                .build();
    }
}
