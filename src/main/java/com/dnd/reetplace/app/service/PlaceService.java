package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.LikeCategory;
import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Search;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.PlaceCategory;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.dto.category.request.LikeCategoryUpdateRequest;
import com.dnd.reetplace.app.dto.category.response.LikeCategoryResponse;
import com.dnd.reetplace.app.dto.place.request.PlaceGetListRequest;
import com.dnd.reetplace.app.dto.place.request.PlaceSearchRequest;
import com.dnd.reetplace.app.dto.place.response.*;
import com.dnd.reetplace.app.repository.*;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
import com.dnd.reetplace.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.dnd.reetplace.app.domain.place.PlaceCategory.REET_PLACE_POPULAR;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceService {

    private final TokenProvider tokenProvider;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final SearchRepository searchRepository;
    private final LikeCategoryRepository likeCategoryRepository;
    private final KakaoHttpRequestService kakaoHttpRequestService;
    private final ScrapService scrapService;

    public static final String MEXICAN_KEYWORD = "멕시칸,브라질";
    public static final String ASIA_KEYWORD = "아시아음식";
    public static final String DEPARTMENT_CATEGORY_NAME = "가정,생활 > 백화점";
    public static final String MARKET_CATEGORY_NAME = "가정,생활 > 시장";
    public static final int SEARCH_HISTORY_MAX_COUNT = 20;

    /**
     * sub category에 해당하는 장소 목록을 카카오 로컬 API를 사용하여 조회한다.
     * 장소는 최소 14개 ~ 최대 20개까지 조회된다.
     *
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param request            조회하고자 하는 장소 및 현재 위치에 대한 정보가 담긴 Request Dto
     * @return 카테고리에 해당하는 장소 목록 (로그인 시 북마크 여부 포함)
     */
    public PlaceGetListResponse getPlaceList(HttpServletRequest httpServletRequest, PlaceGetListRequest request) {
        Member loginMember = findLoginMember(httpServletRequest);

        // 로그인 여부에 따른 subCategory 처리
        List<PlaceSubCategory> subCategory = loginMember == null ?
                request.getSubCategory() : this.getSubCategoryForLoginMember(loginMember.getId(), request.getCategory());

        long size = Math.round(15.0 / subCategory.size());

        // 카카오 서버에서 받아온 장소 목록 collect
        List<KakaoPlaceGetResponse> result =
                request.getCategory().equals(REET_PLACE_POPULAR) ?
                        placeRepository.getReetPlacePopularPlaceList(request.getLat(), request.getLng()) :
                        getPlaceListFromKakao(request, subCategory, size);

        // 북마크 여부 처리
        List<PlaceGetResponse> placeListWithBookmark = updateGetPlaceIsBookmark(loginMember, result);
        return PlaceGetListResponse.of(placeListWithBookmark);
    }

    /**
     * 키워드에 해당하는 장소를 카카오 로컬 API를 사용하여 전국을 기준으로 검색한다.
     * 장소는 최대 15개까지 조회된다. (앱 내 카테고리로 분류되지 못하는 장소의 경우 결과에서 제외된다.)
     *
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param request            검색 키워드, 사용자 위치(lat, lng), 페이지
     * @return 키워드에 해당하는 장소 목록 (로그인 시 북마크 여부 포함)
     */
    @Transactional
    public PlaceSearchListResponse searchPlace(HttpServletRequest httpServletRequest, PlaceSearchRequest request) {
        KakaoPlaceSearchListResponse result = kakaoHttpRequestService.searchPlace(request);
        List<KakaoPlaceSearchResponse> documents = result.getDocuments();
        Member loginMember = findLoginMember(httpServletRequest);
        List<PlaceSearchResponse> placeSearchWithBookmark = this.updateSearchPlaceIsBookmark(loginMember, documents);
        if (loginMember != null) {
            this.updateSearchHistory(loginMember, request.getQuery());
        }
        return PlaceSearchListResponse.of(placeSearchWithBookmark, result.getMeta().getIsEnd());
    }

    public LikeCategoryResponse getLikeCategory(Long memberId, PlaceCategory category) {
        this.validateLoginMember(memberId);// 사용자 유효성 검사
        Optional<LikeCategory> likeCategory = likeCategoryRepository.findByMemberIdAndCategory(memberId, category);
        if (likeCategory.isPresent()) {
            return new LikeCategoryResponse(likeCategory.get().getSubCategory());
        }
        // LikeCategory 테이블에 없는 경우 전체선택으로 판단, 카테고리에 해당하는 모든 하위 카테고리 반환
        List<PlaceSubCategory> subCategoryList = Arrays.stream(PlaceSubCategory.values()).filter(c -> c.getMainCategory().equals(category)).toList();
        return new LikeCategoryResponse(subCategoryList);
    }

    @Transactional
    public void updateLikeCategory(Long memberId, LikeCategoryUpdateRequest request) {
        Member member = this.validateLoginMember(memberId);// 사용자 유효성 검사
        request.getContents().forEach(r -> {
            Optional<LikeCategory> likeCategory = likeCategoryRepository.findByMemberIdAndCategory(memberId, r.getCategory());
            if (likeCategory.isPresent()) {
                likeCategory.get().updateSubCategory(r.getSubCategory());
            } else {
                likeCategoryRepository.save(r.toEntity(member));
            }
        });
    }

    /**
     * 로그인한 사용자에 대해 검색기록을 저장한다.
     * 검색기록 20개 초과 시 가장 오래된 검색기록을 삭제하고 검색기록을 저장한다.
     *
     * @param loginMember
     * @param query
     */
    private void updateSearchHistory(Member loginMember, String query) {
        int searchHistoryCount = searchRepository.countByMemberId(loginMember.getId());
        // 검색기록 20개 초과 시 가장 오래된 검색기록 삭제
        if (searchHistoryCount >= SEARCH_HISTORY_MAX_COUNT) {
            searchRepository.deleteLatestSearchByMemberId(loginMember.getId());
        }
        // 검색기록 저장
        Search search = Search.builder()
                .member(loginMember)
                .query(query)
                .build();
        searchRepository.save(search);
    }

    /**
     * 카카오 로컬 API를 통해 중심좌표 내 1km 반경에 해당하는 sub category 장소 목록을 조회한다.
     * 본 메서드를 통해 sub category에 대해 각각 카카오 로컬 API를 호출하여 결과를 합쳐 반환한다.
     *
     * @param request     조회하고자 하는 장소 및 현재 위치에 대한 정보가 담긴 Request Dto
     * @param subCategory 조회하고자 하는 장소의 서브 카테고리 List
     * @param size        각 카테고리 별 조회하는 장소의 개수
     * @return 카카오에서 제공하는 장소 정보 List
     */
    private List<KakaoPlaceGetResponse> getPlaceListFromKakao(PlaceGetListRequest request, List<PlaceSubCategory> subCategory, long size) {
        ArrayList<KakaoPlaceGetResponse> result = new ArrayList<>();
        subCategory.forEach(category -> {
            switch (category) {
                case WORLD -> {
                    long newSize = Math.round(size / 2.0);
                    result.addAll(kakaoHttpRequestService.getPlaceListKeyword(MEXICAN_KEYWORD, request, category, newSize));
                    result.addAll(kakaoHttpRequestService.getPlaceListKeyword(ASIA_KEYWORD, request, category, newSize));
                }
                case DEPARTMENT_STORE -> {
                    List<KakaoPlaceGetResponse> placeList =
                            kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, 15)
                                    .stream().filter(place -> place.getCategory_name().contains(DEPARTMENT_CATEGORY_NAME))
                                    .limit(size)
                                    .toList();
                    result.addAll(placeList);
                }
                case MARKET -> {
                    List<KakaoPlaceGetResponse> placeList =
                            kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, 15)
                                    .stream().filter(place -> place.getCategory_name().contains(MARKET_CATEGORY_NAME))
                                    .limit(size)
                                    .toList();
                    result.addAll(placeList);
                }
                case MART -> result.addAll(kakaoHttpRequestService.getPlaceListCategory(
                                PlaceCategoryGroupCode.MT1.name(),
                                request,
                                category,
                                size
                        )
                );
                default ->
                        result.addAll(kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, size));
            }
        });
        return result;
    }

    /**
     * (로그인 시) 장소 목록 조회에서 각 장소 별 북마크 여부 및 북마크 id를 업데이트한다.
     * 또한, 카카오에서 제공하는 장소 정보 -> 앱 내에서 필요한 정보만 포함하는 Custom Response로 결과를 변환하여 반환한다.
     *
     * @param loginMember 로그인 한 사용자 엔티티 (비로그인 사용자 경우 null)
     * @param result      카카오 로컬 API를 통해 받아온 카카오에서 제공하는 장소 정보 List
     * @return 북마크 여부 및 북마크 id가 업데이트 된 Custom Place Response List
     */
    private List<PlaceGetResponse> updateGetPlaceIsBookmark(Member loginMember, List<KakaoPlaceGetResponse> result) {
        if (loginMember == null) {
            return result.stream()
                    .map(response -> PlaceGetResponse.ofWithoutBookmark(response, scrapService.getPlaceThumbnailUrl(response.getId())))
                    .toList();
        }
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByMember(loginMember.getId());
        return result.stream()
                .map(place -> bookmarkList.stream()
                        .filter(bookmark ->
                                Objects.equals(bookmark.getPlace().getKakaoPid(), place.getId()))
                        .findFirst()
                        .map(bookmark -> PlaceGetResponse.of(
                                place,
                                bookmark.getThumbnailUrl(),
                                bookmark.getType(),
                                bookmark.getId()))
                        .orElse(PlaceGetResponse.ofWithoutBookmark(place, scrapService.getPlaceThumbnailUrl(place.getId()))))
                .toList();
    }

    /**
     * (로그인 시) 장소 검색에서 각 장소 별 북마크 여부 및 북마크 id, 릿플점수를 업데이트한다.
     * 또한, 카카오에서 제공하는 장소 정보 -> 앱 내에서 필요한 정보만 포함하는 Custom Response로 결과를 변환하여 반환한다.
     * 이 때, 앱 내 카테고리로 분류되지 못하는 장소의 경우 결과에서 제외된다.
     *
     * @param loginMember 로그인 사용자 객체 (비로그인 시 null)
     * @param result      카카오 로컬 API를 통해 받아온 카카오에서 제공하는 장소 정보 List
     * @return 북마크 여부, 북마크 id, 릿플점수가 업데이트 된 Custom Place Response List
     */
    private List<PlaceSearchResponse> updateSearchPlaceIsBookmark(Member loginMember, List<KakaoPlaceSearchResponse> result) {
        if (loginMember == null) {
            return result.stream()
                    .map(response -> PlaceSearchResponse.ofWithoutBookmark(response, scrapService.getPlaceThumbnailUrl(response.getId())))
                    .filter(place -> place.getCategory() != null)
                    .toList();
        }
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByMember(loginMember.getId());
        return result.stream()
                .map(place -> bookmarkList.stream()
                        .filter(bookmark ->
                                Objects.equals(bookmark.getPlace().getKakaoPid(), place.getId()))
                        .findFirst()
                        .map(bookmark -> PlaceSearchResponse.of(
                                place,
                                bookmark.getThumbnailUrl(),
                                bookmark.getType(),
                                bookmark.getId(),
                                bookmark.getRate()))
                        .orElse(PlaceSearchResponse.ofWithoutBookmark(place, scrapService.getPlaceThumbnailUrl(place.getId()))))
                .filter(place -> place.getCategory() != null)
                .toList();
    }

    /**
     * HttpServletRequest 내 Authorization Header를 파싱하여 로그인 여부를 판단한다.
     *
     * @param request 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @return 로그인 시 해당하는 Member 엔티티 반환, 비로그인 시 null 반환
     */
    private Member findLoginMember(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        if (token != null) {
            String uid = tokenProvider.getUid(token);
            LoginType loginType = tokenProvider.getLoginType(token);
            return memberRepository.findByUidAndLoginTypeAndDeletedAtIsNull(uid, loginType)
                    .orElseThrow(() -> new MemberUidNotFoundException(uid));
        }
        return null;
    }

    /**
     * memberId를 통해 사용자 유효성을 검사한다.
     *
     * @param memberId 사용자 id
     */
    private Member validateLoginMember(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
    }

    private List<PlaceSubCategory> getSubCategoryForLoginMember(Long memberId, PlaceCategory category) {
        Optional<LikeCategory> likeCategory = likeCategoryRepository.findByMemberIdAndCategory(memberId, category);
        // likeCategory 존재할 경우 필터 설정 case로, 사용자 설정된 하위 카테고리 반환
        if (likeCategory.isPresent() && !likeCategory.get().getSubCategory().isEmpty()) {
            return likeCategory.get().getSubCategory();
        }
        // likeCategory 존재하지 않거나 subCategory 비어있을 경우 전체 선택 case로, 상위 카테고리에 해당하는 모든 하위 카테고리 반환
        return Arrays.stream(PlaceSubCategory.values()).filter(c -> c.getMainCategory().equals(category)).toList();
    }
}
