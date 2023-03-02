package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.bookmark.Bookmark;
import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.dto.place.request.PlaceGetListRequest;
import com.dnd.reetplace.app.dto.place.response.KakaoPlaceResponse;
import com.dnd.reetplace.app.dto.place.response.PlaceGetListResponse;
import com.dnd.reetplace.app.dto.place.response.PlaceGetResponse;
import com.dnd.reetplace.app.repository.BookmarkRepository;
import com.dnd.reetplace.app.repository.MemberRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceService {

    private final TokenProvider tokenProvider;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final KakaoHttpRequestService kakaoHttpRequestService;

    public static final String MEXICAN_KEYWORD = "멕시칸,브라질";
    public static final String ASIA_KEYWORD = "아시아음식";
    public static final String DEPARTMENT_CATEGORY_NAME = "가정,생활 > 백화점";
    public static final String MARKET_CATEGORY_NAME = "가정,생활 > 시장";

    public PlaceGetListResponse getPlaceList(
            HttpServletRequest httpServletRequest,
            PlaceGetListRequest request) {

        List<PlaceSubCategory> subCategory = request.getSubCategory();
        long size = Math.round(15.0 / subCategory.size());
        ArrayList<KakaoPlaceResponse> result = new ArrayList<>();

        // 카카오 서버에서 받아온 장소 목록 collect
        subCategory.forEach(category -> {
            if (category.equals(PlaceSubCategory.FOOD_WORLD)) {
                long newSize = Math.round(size / 2.0);
                result.addAll(kakaoHttpRequestService.getPlaceListKeyword(MEXICAN_KEYWORD, request, category, newSize));
                result.addAll(kakaoHttpRequestService.getPlaceListKeyword(ASIA_KEYWORD, request, category, newSize));
            } else if (category.equals(PlaceSubCategory.SHOPPING_DEPARTMENT_STORE)) {
                List<KakaoPlaceResponse> placeList =
                        kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, 15)
                                .stream().filter(place -> place.getCategory_name().contains(DEPARTMENT_CATEGORY_NAME))
                                .limit(size)
                                .toList();
                result.addAll(placeList);
            } else if (category.equals(PlaceSubCategory.SHOPPING_MARKET)) {
                List<KakaoPlaceResponse> placeList =
                        kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, 15)
                                .stream().filter(place -> place.getCategory_name().contains(MARKET_CATEGORY_NAME))
                                .limit(size)
                                .toList();
                result.addAll(placeList);
            } else if (category.equals(PlaceSubCategory.SHOPPING_MART)) {
                result.addAll(kakaoHttpRequestService.getPlaceListCategory(
                                PlaceCategoryGroupCode.MT1.name(),
                                request,
                                category,
                                size
                        )
                );
            } else {
                result.addAll(kakaoHttpRequestService.getPlaceListKeyword(category.getDescription(), request, category, size));
            }
        });

        // 북마크 여부 처리
        Member loginMember = findLoginMember(httpServletRequest);
        if (loginMember == null) {
            List<PlaceGetResponse> placeList =
                    result.stream().map(place ->
                            PlaceGetResponse.of(place, null, null)
                    ).toList();
            return PlaceGetListResponse.of(placeList);
        } else {
            List<Bookmark> bookmarkList = bookmarkRepository.findAllByMember(loginMember.getId());
            List<PlaceGetResponse> placeList =
                    result.stream().map(place -> {
                        Optional<Bookmark> bookmark = bookmarkList.stream()
                                .filter(b -> Objects.equals(b.getPlace().getKakaoPid(), place.getId()))
                                .findFirst();
                        if (bookmark.isPresent()) {
                            return PlaceGetResponse.of(
                                    place,
                                    bookmark.get().getType(),
                                    bookmark.get().getId()
                            );
                        } else {
                            return PlaceGetResponse.of(
                                    place,
                                    null,
                                    null
                            );
                        }
                    }).toList();
            return PlaceGetListResponse.of(placeList);
        }
    }

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
