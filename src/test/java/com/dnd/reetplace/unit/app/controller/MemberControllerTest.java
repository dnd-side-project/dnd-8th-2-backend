package com.dnd.reetplace.unit.app.controller;

import com.dnd.reetplace.app.config.SecurityConfig;
import com.dnd.reetplace.app.controller.MemberController;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.service.MemberService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        })
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private TokenProvider tokenProvider;

    @DisplayName("로그인한 사용자 정보 조회에 성공한다.")
    @Test
    void givenMemberDetails_whenGetMyProfile_thenSuccess() throws Exception {

        // given
        MemberDetails memberDetails = new MemberDetails(createMockMember(1L));
        given(memberService.getMemberInfo(any()))
                .willReturn(MemberDto.from(memberDetails.getMember()));

        // when & then
        mvc.perform(get("/api/members/my")
                        .with(user(memberDetails))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(memberDetails.getUid()))
                .andExpect(jsonPath("$.nickname").value(memberDetails.getMember().getNickname()));
    }

    private Member createMockMember(Long id) {
        return Member.builder()
                .id(id)
                .uid("testUid" + id)
                .loginType(LoginType.KAKAO)
                .nickname("test" + id)
                .build();
    }
}