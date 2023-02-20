package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.dto.auth.response.KakaoProfileResponse;
import com.dnd.reetplace.global.exception.auth.KakaoUnauthorizedException;
import com.dnd.reetplace.global.log.LogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
public class OAuth2HttpRequestService {

    public static final String KAKAO_GET_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoProfileResponse getKakaoProfile(String accessToken) {
        try {
            // Header 추가
            HttpHeaders header = new HttpHeaders();
            header.add(AUTHORIZATION, "Bearer " + accessToken);
            header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // request 구성
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(header);

            // API 요청
            ResponseEntity<String> response = rt.exchange(
                    KAKAO_GET_PROFILE_URL,
                    HttpMethod.POST,
                    httpRequest,
                    String.class
            );

            // response 객체에 매핑
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), KakaoProfileResponse.class);
        } catch (Exception e) {
            log.error("[{}] OAuth2Service.kakaoLogin() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new KakaoUnauthorizedException();
        }
    }
}