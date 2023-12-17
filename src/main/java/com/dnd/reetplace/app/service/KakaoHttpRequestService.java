package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.place.PlaceSubCategory;
import com.dnd.reetplace.app.dto.place.request.PlaceGetListRequest;
import com.dnd.reetplace.app.dto.place.request.PlaceSearchRequest;
import com.dnd.reetplace.app.dto.place.response.KakaoPlaceGetListResponse;
import com.dnd.reetplace.app.dto.place.response.KakaoPlaceGetResponse;
import com.dnd.reetplace.app.dto.place.response.KakaoPlaceSearchListResponse;
import com.dnd.reetplace.app.dto.place.response.KakaoPlaceSearchResponse;
import com.dnd.reetplace.global.exception.place.PlaceKakaoApiBadRequestException;
import com.dnd.reetplace.global.log.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
public class KakaoHttpRequestService {

    @Value("${kakao.rest-api-key:none}")
    public String restApiKey;

    public static final String GET_PLACE_KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword";
    public static final String GET_PLACE_CATEGORY_URL = "https://dapi.kakao.com/v2/local/search/category";

    public List<KakaoPlaceGetResponse> getPlaceListKeyword(
            String query,
            PlaceGetListRequest request,
            PlaceSubCategory subCategory,
            long size
    ) {
        try {
            // Header 추가
            HttpHeaders header = new HttpHeaders();
            header.add(AUTHORIZATION, "KakaoAK " + restApiKey);
            header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // request 구성
            UriComponents uriBuilder = UriComponentsBuilder
                    .fromUriString(GET_PLACE_KEYWORD_URL)
                    .queryParam("query", query)
                    .queryParam("radius", 1000)
                    .queryParam("size", size)
                    .queryParam("x", request.getLng())
                    .queryParam("y", request.getLat())
                    .build();
            HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(header);
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            // API 요청
            ResponseEntity<KakaoPlaceGetListResponse> response = rt.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    httpRequest,
                    KakaoPlaceGetListResponse.class
            );
            List<KakaoPlaceGetResponse> placeList = response.getBody().getDocuments();
            placeList.forEach(place ->
                    place.updateCategory(request.getCategory(), subCategory)
            );
            return placeList;
        } catch (Exception e) {
            log.error("[{}] KakaoHttpRequestService.getPlaceListKeyword() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new PlaceKakaoApiBadRequestException();
        }
    }

    public List<KakaoPlaceGetResponse> getPlaceListCategory(
            String categoryGroupCode,
            PlaceGetListRequest request,
            PlaceSubCategory subCategory,
            long size
    ) {
        try {
            // Header 추가
            HttpHeaders header = new HttpHeaders();
            header.add(AUTHORIZATION, "KakaoAK " + restApiKey);
            header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // request 구성
            UriComponents uriBuilder = UriComponentsBuilder
                    .fromUriString(GET_PLACE_CATEGORY_URL)
                    .queryParam("category_group_code", categoryGroupCode)
                    .queryParam("radius", 1000)
                    .queryParam("size", size)
                    .queryParam("x", request.getLng())
                    .queryParam("y", request.getLat())
                    .build();
            HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(header);
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            // API 요청
            ResponseEntity<KakaoPlaceGetListResponse> response = rt.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    httpRequest,
                    KakaoPlaceGetListResponse.class
            );
            List<KakaoPlaceGetResponse> placeList = response.getBody().getDocuments();
            placeList.forEach(place ->
                    place.updateCategory(request.getCategory(), subCategory)
            );
            return placeList;
        } catch (Exception e) {
            log.error("[{}] KakaoHttpRequestService.getPlaceListCategory() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new PlaceKakaoApiBadRequestException();
        }
    }

    public KakaoPlaceSearchListResponse searchPlace(PlaceSearchRequest request) {
        try {
            // Header 추가
            HttpHeaders header = new HttpHeaders();
            header.add(AUTHORIZATION, "KakaoAK " + restApiKey);
            header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // request 구성
            UriComponents uriBuilder = UriComponentsBuilder
                    .fromUriString(GET_PLACE_KEYWORD_URL)
                    .queryParam("query", request.getQuery())
                    .queryParam("page", request.getPage())
                    .queryParam("x", request.getLng())
                    .queryParam("y", request.getLat())
                    .queryParam("radius", 1000)
                    .build();

            HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(header);
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            // API 요청
            ResponseEntity<KakaoPlaceSearchListResponse> response = rt.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    httpRequest,
                    KakaoPlaceSearchListResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("[{}] KakaoHttpRequestService.searchPlace() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new PlaceKakaoApiBadRequestException();
        }
    }
}
