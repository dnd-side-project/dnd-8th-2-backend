package com.dnd.reetplace.unit.app.controller;

import com.dnd.reetplace.app.config.SecurityConfig;
import com.dnd.reetplace.app.controller.BookmarkController;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.BookMarkRelLink;
import com.dnd.reetplace.app.domain.place.Address;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.domain.place.Point;
import com.dnd.reetplace.app.dto.bookmark.BookmarkDto;
import com.dnd.reetplace.app.dto.bookmark.request.BookmarkCreateRequest;
import com.dnd.reetplace.app.dto.bookmark.response.NumOfBookmarksResponse;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.place.PlaceDto;
import com.dnd.reetplace.app.dto.place.request.PlaceRequest;
import com.dnd.reetplace.app.service.BookmarkService;
import com.dnd.reetplace.app.service.ScrapService;
import com.dnd.reetplace.app.type.*;
import com.dnd.reetplace.global.security.JwtAuthenticationFilter;
import com.dnd.reetplace.global.security.MemberDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(
        controllers = BookmarkController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
class BookmarkControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    BookmarkService bookmarkService;
    @MockBean
    ScrapService scrapService;

    BookmarkControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("북마크 생성을 위한 정보가 주어지고 북마크를 생성하면 생성된 북마크를 저장 후 반환한다.")
    @Test
    void givenBookmarkCreateInfo_whenMarking_thenSaveAndReturnBookmark() throws Exception {
        // given
        Long memberId = 1L;
        Long expectedBookmarkId = 2L;
        given(bookmarkService.save(eq(memberId), any(BookmarkDto.class)))
                .willReturn(createSavedBookmarkDto(expectedBookmarkId));
        given(scrapService.getPlaceThumbnailUrl(any(String.class)))
                .willReturn("https://place...");

        // when & then
        mvc.perform(
                        post("/api/bookmarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createBookmarkCreateRequest()))
                                .with(csrf())
                                .with(user(new MemberDetails(createMember())))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedBookmarkId));
    }

    @DisplayName("북마크 개수를 조회하면, 조회된 북마크 개수가 반환됩니다.")
    @Test
    void given_whenGettingNumOfBookmarks_thenReturnNumOfBookmarks() throws Exception {
        // given
        long memberId = 1L;
        int expectedNumOfAll = 5;
        int expectedNumOfWant = 3;
        int expectedNumOfDone = 2;
        NumOfBookmarksResponse expectedResult = new NumOfBookmarksResponse(expectedNumOfAll, expectedNumOfWant, expectedNumOfDone);
        given(bookmarkService.getNumOfBookmarks(memberId))
                .willReturn(expectedResult);

        // when & then
        mvc.perform(
                        get("/api/bookmarks/counts")
                                .with(csrf())
                                .with(user(new MemberDetails(createMember())))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.numOfAll").value(expectedNumOfAll))
                .andExpect(jsonPath("$.numOfWant").value(expectedNumOfWant))
                .andExpect(jsonPath("$.numOfDone").value(expectedNumOfDone));
    }

    @DisplayName("주어진 검색 정보/조건으로 검색을 하면 북마크 목록을 반환한다.")
    @Test
    void givenSearchConditions_whenSearching_thenReturnBookmarksSlice() throws Exception {
        // given
        Long memberId = 1L;
        given(bookmarkService.searchBookmarks(
                memberId,
                BookmarkSearchType.ALL,
                BookmarkSearchSort.LATEST,
                Pageable.ofSize(20))
        ).willReturn(Page.empty());
        given(scrapService.getPlaceThumbnailUrl(any(String.class)))
                .willReturn("https://place...");

        // when & then
        mvc.perform(get("/api/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchType", "ALL")
                .with(csrf())
                .with(user(new MemberDetails(createMember())))
        ).andExpect(status().isOk());
    }

    @DisplayName("삭제할 북마크의 PK가 주어지고, 삭제하면, 북마크가 삭제된다.")
    @Test
    void givenBookmarkId_whenDeleting_thenDeleteBookmark() throws Exception {
        // given
        long bookmarkId = 1L;
        willDoNothing().given(bookmarkService).delete(any(Long.class), eq(bookmarkId));

        // when & then
        mvc.perform(delete("/api/bookmarks/" + bookmarkId)
                .with(csrf())
                .with(user(new MemberDetails(createMember())))
        ).andExpect(status().isOk());
    }

    private Member createMember() {
        Member member = Member.builder()
                .uid("uid")
                .loginType(LoginType.KAKAO)
                .nickname("nickname")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        return member;
    }

    private MemberDto createSavedMemberDto() {
        return MemberDto.from(createMember());
    }

    private BookmarkDto createSavedBookmarkDto() {
        return createSavedBookmarkDto(1L);
    }

    private BookmarkDto createSavedBookmarkDto(Long bookmarkId) {
        return BookmarkDto.of(
                bookmarkId,
                createSavedMemberDto(),
                createSavedPlaceDto(),
                BookmarkType.WANT,
                "https://thumbnail-image-url",
                (short) 2,
                "people",
                new BookMarkRelLink(null, null, null),
                LocalDateTime.of(2023, 3, 5, 0, 0),
                LocalDateTime.of(2023, 3, 5, 0, 0)
        );
    }

    private PlaceDto createSavedPlaceDto() {
        return PlaceDto.of(
                1L,
                "kakao pid",
                "name",
                "url",
                "category1 > category2 > category3",
                PlaceCategoryGroupCode.CT1,
                PlaceCategory.CULTURE,
                PlaceSubCategory.CINEMA,
                "010-1234-5678",
                new Address(
                        "sido sgg detail",
                        "sido sgg road_detail"
                ),
                new Point("1.23", "4.56"),
                LocalDateTime.of(2023, 3, 5, 0, 0),
                LocalDateTime.of(2023, 3, 5, 0, 0),
                LocalDateTime.of(2023, 3, 5, 0, 0)
        );
    }

    private BookmarkCreateRequest createBookmarkCreateRequest() {
        return new BookmarkCreateRequest(
                createPlaceRequest(),
                BookmarkType.WANT,
                (short) 2,
                "people",
                null,
                null,
                null
        );
    }

    private PlaceRequest createPlaceRequest() {
        return new PlaceRequest(
                "kakao pid",
                "name",
                "url",
                "category1 > category2 > category3",
                PlaceCategoryGroupCode.CT1,
                PlaceCategory.CULTURE,
                PlaceSubCategory.CINEMA,
                "010-1234-5678",
                "sido sgg lot_number_detail",
                "sido sgg road_detail",
                "1.23",
                "4.56"
        );
    }
}