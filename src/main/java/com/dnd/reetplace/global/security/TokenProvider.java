package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.app.type.RoleType;
import com.dnd.reetplace.global.exception.member.MemberUidNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    // Expiration Time
    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long AT_EXP_TIME = 12 * HOUR; // Access Token 만료시간 : 12시간
    public static final long RT_EXP_TIME = 14 * DAY; // Refresh Token 만료시간 : 2주

    // JWT Secret
    @Value("${jwt.secret:reet_place_jwt_secret_local_sec_e32}")
    public String secret;

    public Key secretKey;

    // Header
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";

    // Claim Key
    public static final String ROLE_CLAIM_KEY = "role";
    public static final String LOGIN_TYPE_CLAIM_KEY = "login_type";

    private final MemberRepository memberRepository;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * HttpServletRequest 내 Header에 담겨있는 token의 값을 파싱해 반환한다.
     *
     * @param request token을 Header로 담고있는 HttpServletRequest 객체
     * @return Header에 담겨있는 Token
     */
    public String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
    }

    /**
     * Access token에 해당하는 사용자에 대한 Authentication 객체를 반환한다.
     *
     * @param accessToken JWT Access Token
     * @return Access token에서 조회한 사용자에 대한 Authentication 객체
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaim(accessToken);
        String uid = claims.getSubject();
        LoginType loginType = Arrays.stream(LoginType.values())
                .filter(t -> t.name().equals(claims.get(LOGIN_TYPE_CLAIM_KEY)))
                .findFirst()
                .get();
        Member member = memberRepository.findByUidAndLoginType(uid, loginType)
                .orElseThrow(() -> new MemberUidNotFoundException(uid));
        MemberDetails memberDetails = new MemberDetails(member);
        return new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());
    }

    /**
     * 사용자에 대한 정보를 기반으로 Access Token을 생성해 반환한다.
     *
     * @param member 사용자 (Member)
     * @return 생성된 Access Token
     */
    public String createAccessToken(Member member) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(member.getUid())
                .claim(ROLE_CLAIM_KEY, RoleType.NORMAL.getKey())
                .claim(LOGIN_TYPE_CLAIM_KEY, member.getLoginType().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + AT_EXP_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 사용자에 대한 정보를 기반으로 Refresh Token을 생성해 반환한다.
     *
     * @param member 사용자 (Member)
     * @return 생성된 Refresh Token
     */
    public String createRefreshToken(Member member) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(member.getUid())
                .claim(ROLE_CLAIM_KEY, RoleType.NORMAL.getKey())
                .claim(LOGIN_TYPE_CLAIM_KEY, member.getLoginType().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + RT_EXP_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token에서 claim을 추출한다.
     *
     * @param token JWT Token
     * @return accessToken에서 추출한 claim
     */
    public Claims getClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Token을 검증한다. (유효성, 만료여부)
     *
     * @param token 검증하고자 하는 Token
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            log.error("TokenProvider.validateToken() ex={}", String.valueOf(e));
            throw e;
        }
    }
}
