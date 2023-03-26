package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.dto.auth.response.ApplePublicKeyResponse;
import com.dnd.reetplace.app.dto.auth.response.KakaoProfileResponse;
import com.dnd.reetplace.global.exception.auth.ApplePublicKeyNotFoundException;
import com.dnd.reetplace.global.exception.auth.AppleUnauthorizedException;
import com.dnd.reetplace.global.exception.auth.KakaoUnauthorizedException;
import com.dnd.reetplace.global.log.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
public class OAuth2HttpRequestService {

    public static final String KAKAO_GET_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_UNLINK = "https://kapi.kakao.com/v1/user/unlink";

    public static final String APPLE_GET_PUBLIC_KEY_URL = "https://appleid.apple.com/auth/keys";

    /**
     * 카카오 서버와 통신하여 카카오 access token에 해당하는 사용자 프로필을 가져온다.
     *
     * @param accessToken 카카오 access token
     * @return access token에 해당하는 사용자 카카오 프로필
     */
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

    /**
     * 애플 서버와 통신하여 애플 identity token에 해당하는 사용자 애플 uid를 가져온다.
     *
     * @param identityToken 애플 identity token
     * @return identity token에 해당하는 사용자 애플 uid
     */
    public String getAppleUid(String identityToken) {
        try {
            RestTemplate rt = new RestTemplate();

            // Public Key 목록 조회
            ApplePublicKeyResponse response = rt.getForObject(APPLE_GET_PUBLIC_KEY_URL, ApplePublicKeyResponse.class);

            // identity token과 Public Key 목록 매칭을 통해 publicKey 조회
            PublicKey publicKey = getApplePublicKey(identityToken, response);

            // publicKey를 통해 identity token payload 디코딩
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();

            return claims.getSubject();
        } catch (ApplePublicKeyNotFoundException e) {
            log.error("[{}] OAuth2Service.appleLogin() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw e;
        } catch (Exception e) {
            log.error("[{}] OAuth2Service.appleLogin() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new AppleUnauthorizedException();
        }
    }

    /**
     * 애플 identity token 내 key와 애플 서버에서 받아온 PublicKey 목록을 매칭하여 PublicKey를 반환한다.
     *
     * @param identityToken 애플 identity token
     * @param response      애플 서버에서 받아온 PublicKey 목록
     * @return identity token 내 key와 PublicKey 목록에서 매칭된 PublicKey
     */
    private PublicKey getApplePublicKey(String identityToken, ApplePublicKeyResponse response) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Identity Token 헤더 Base64 디코딩
        String tokenHeaderString = identityToken.substring(0, identityToken.indexOf("."));
        Map<String, String> header =
                new ObjectMapper().readValue(
                        new String(Base64.getDecoder().decode(tokenHeaderString), UTF_8),
                        Map.class
                );

        // Apple Public Key와 매칭되는 n, e 파싱
        ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                .orElseThrow(ApplePublicKeyNotFoundException::new);

        // n, e 값 Base64 디코딩
        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        // n, e 값 기반으로 publicKey 가져옴
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * 카카오 서버와 통신하여 카카오 access token에 해당하는 사용자의 연결을 끊는다.
     *
     * @param accessToken 카카오 access token
     */
    public void unlinkKakao(String accessToken) {
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
            rt.exchange(
                    KAKAO_UNLINK,
                    HttpMethod.POST,
                    httpRequest,
                    String.class
            );
        } catch (Exception e) {
            log.error("[{}] OAuth2Service.unlinkKakao() ex={}", LogUtils.getLogTraceId(), String.valueOf(e));
            throw new KakaoUnauthorizedException();
        }
    }
}
