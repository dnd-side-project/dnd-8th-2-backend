package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.auth.KakaoProfileResponse;
import com.dnd.reetplace.app.dto.auth.LoginResponse;
import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import com.dnd.reetplace.app.dto.auth.TokenResponse;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.auth.KakaoUnauthorizedException;
import com.dnd.reetplace.global.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class OAuth2Service {

    public static final String GET_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final TokenProvider tokenProvider;

    /**
     * 카카오 서버에서 받은 access token을 기반으로 사용자 프로필을 받아온 후, 로그인 (또는 회원가입)을 진행한다.
     *
     * @param request Authorization Header(kakao access token) 가 포함된 HttpServletRequest 객체
     * @return 로그인 (또는 회원가입) 완료 후 사용자 정보 (memberId, uid, accessToken, refreshToken 등)
     */
    @Transactional
    public LoginResponse kakaoLogin(HttpServletRequest request) throws JsonProcessingException {

        String token = tokenProvider.getToken(request);

        // Header 추가
        HttpHeaders header = new HttpHeaders();
        header.add(AUTHORIZATION, "Bearer " + token);
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // request 구성
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(header);

        // API 요청
        ResponseEntity<String> response;
        try {
            response = rt.exchange(
                    GET_PROFILE_URL,
                    HttpMethod.POST,
                    httpRequest,
                    String.class
            );
        } catch (Exception e) {
            log.error("OAuth2Service.kakaoLogin() ex={}", String.valueOf(e));
            throw new KakaoUnauthorizedException();
        }

        // response 객체에 매핑
        ObjectMapper mapper = new ObjectMapper();
        KakaoProfileResponse kakaoProfile = mapper.readValue(response.getBody(), KakaoProfileResponse.class);
        String uid = kakaoProfile.getId().toString();

        // 로그인 또는 회원가입 처리
        Member member = memberRepository.findByUidAndLoginType(uid, LoginType.KAKAO)
                .orElse(null);

        if (member == null) {
            String email = (String) kakaoProfile.getKakao_account().get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoProfile.getKakao_account().get("profile");
            member = Member.builder()
                    .uid(uid)
                    .loginType(LoginType.KAKAO)
                    .email(email)
                    .nickname((String) profile.get("nickname"))
                    .build();
            memberRepository.save(member);
        }
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid(), member.getLoginType());
        refreshTokenRedisService.saveRefreshToken(member.getUid(), refreshToken);
        return LoginResponse.of(member, accessToken, refreshToken);
    }

    /**
     * Authorization Header에 포함된 refresh token을 통해 access token 및 refresh token을 재발급한다.
     *
     * @param request Authorization Header(refresh token) 가 포함된 HttpServletRequest 객체
     * @return 재발급된 access token 및 refresh token
     */
    @Transactional
    public TokenResponse refresh(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        tokenProvider.validateToken(token);
        LoginType loginType = tokenProvider.getLoginType(token);
        RefreshTokenDto refreshTokenDto = refreshTokenRedisService.findRefreshToken(token);
        String accessToken = tokenProvider.createAccessToken(refreshTokenDto.getUid(), loginType);
        String refreshToken = tokenProvider.createRefreshToken(refreshTokenDto.getUid(), loginType);
        refreshTokenRedisService.saveRefreshToken(refreshTokenDto.getUid(), refreshToken);
        return TokenResponse.of(accessToken, refreshToken);
    }
}
