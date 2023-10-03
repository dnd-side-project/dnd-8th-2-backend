package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.dto.place.request.PlaceGetListRequest;
import com.dnd.reetplace.app.dto.place.response.*;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.PlaceRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.app.type.PlaceCategoryGroupCode;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
import com.dnd.reetplace.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dnd.reetplace.app.domain.place.PlaceCategory.REET_PLACE_POPULAR;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceService {

    private final TokenProvider tokenProvider;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final KakaoHttpRequestService kakaoHttpRequestService;
    private final ScrapService scrapService;

    public static final String MEXICAN_KEYWORD = "멕시칸,브라질";
    public static final String ASIA_KEYWORD = "아시아음식";
    public static final String DEPARTMENT_CATEGORY_NAME = "가정,생활 > 백화점";
    public static final String MARKET_CATEGORY_NAME = "가정,생활 > 시장";

    /**
     * sub category에 해당하는 장소 목록을 카카오 로컬 API를 사용하여 조회한다.
     * 장소는 최소 14개 ~ 최대 20개까지 조회된다.
     *
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param request            조회하고자 하는 장소 및 현재 위치에 대한 정보가 담긴 Request Dto
     * @return 카테고리에 해당하는 장소 목록 (로그인 시 북마크 여부 포함)
     */
    public PlaceGetListResponse getPlaceList(
            HttpServletRequest httpServletRequest,
            PlaceGetListRequest request) {

        List<PlaceSubCategory> subCategory = request.getSubCategory();
        long size = Math.round(15.0 / subCategory.size());

        // 카카오 서버에서 받아온 장소 목록 collect
        List<KakaoPlaceGetResponse> result =
                request.getCategory().equals(REET_PLACE_POPULAR) ?
                        placeRepository.getReetPlacePopularPlaceList(request.getLat(), request.getLng()) :
                        getPlaceListFromKakao(request, subCategory, size);

        // 북마크 여부 처리
        List<PlaceGetResponse> placeListWithBookmark = updateGetPlaceIsBookmark(httpServletRequest, result);
        return PlaceGetListResponse.of(placeListWithBookmark);
    }

    /**
     * 키워드에 해당하는 장소를 카카오 로컬 API를 사용하여 전국을 기준으로 검색한다.
     * 장소는 최대 15개까지 조회된다. (앱 내 카테고리로 분류되지 못하는 장소의 경우 결과에서 제외된다.)
     *
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param query              검색하고자 하는 키워드
     * @param page               결과 페이지
     * @return 키워드에 해당하는 장소 목록 (로그인 시 북마크 여부 포함)
     */
    public PlaceSearchListResponse searchPlace(HttpServletRequest httpServletRequest, String query, int page) {
        List<KakaoPlaceSearchResponse> result = kakaoHttpRequestService.searchPlace(query, page);
        List<PlaceSearchResponse> placeSearchWithBookmark = updateSearchPlaceIsBookmark(httpServletRequest, result);
        return PlaceSearchListResponse.of(placeSearchWithBookmark);
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
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param result             카카오 로컬 API를 통해 받아온 카카오에서 제공하는 장소 정보 List
     * @return 북마크 여부 및 북마크 id가 업데이트 된 Custom Place Response List
     */
    private List<PlaceGetResponse> updateGetPlaceIsBookmark(HttpServletRequest httpServletRequest, List<KakaoPlaceGetResponse> result) {
        Member loginMember = findLoginMember(httpServletRequest);
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
     * @param httpServletRequest 로그인 여부를 판단하기 위한 HttpServletRequest 객체
     * @param result             카카오 로컬 API를 통해 받아온 카카오에서 제공하는 장소 정보 List
     * @return 북마크 여부, 북마크 id, 릿플점수가 업데이트 된 Custom Place Response List
     */
    private List<PlaceSearchResponse> updateSearchPlaceIsBookmark(HttpServletRequest httpServletRequest, List<KakaoPlaceSearchResponse> result) {
        Member loginMember = findLoginMember(httpServletRequest);
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
}
