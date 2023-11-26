package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Survey;
import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import com.dnd.reetplace.app.dto.auth.response.KakaoProfileResponse;
import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.survey.SurveyDto;
import com.dnd.reetplace.app.dto.survey.request.SurveyListRequest;
import com.dnd.reetplace.app.dto.survey.request.SurveyRequest;
import com.dnd.reetplace.app.repository.MemberRepository;
import com.dnd.reetplace.app.repository.SurveyRepository;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.global.exception.member.MemberIdNotFoundException;
import com.dnd.reetplace.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OAuth2Service {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final TokenProvider tokenProvider;
    private final OAuth2HttpRequestService httpRequestService;

    /**
     * 카카오 서버에서 받은 access token을 기반으로 사용자 프로필을 받아온 후, 로그인 (또는 회원가입)을 진행한다.
     *
     * @param token kakao access token
     * @return 로그인 (또는 회원가입) 완료 후 사용자 정보 (memberId, uid, accessToken, refreshToken 등)
     */
    @Transactional
    public LoginResponse kakaoLogin(String token) {

        // 카카오 사용자 프로필 획득
        KakaoProfileResponse kakaoProfile = httpRequestService.getKakaoProfile(token);

        // 로그인 또는 회원가입 처리
        Member member = loginOrSignup(kakaoProfile.getId().toString(), kakaoProfile.getNickname(), kakaoProfile.getEmail(), LoginType.KAKAO);
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid(), member.getLoginType());
        refreshTokenRedisService.saveRefreshToken(member.getUid(), refreshToken);
        return LoginResponse.of(MemberDto.from(member), accessToken, refreshToken);
    }

    /**
     * 애플 서버에서 받은 identity token을 기반으로 사용자 프로필을 받아온 후, 로그인 (또는 회원가입)을 진행한다.
     *
     * @param token    apple identity token
     * @param nickname 애플 서버에서 받은 사용자 이름
     * @return 로그인 (또는 회원가입) 완료 후 사용자 정보 (memberId, uid, accessToken, refreshToken 등)
     */
    @Transactional
    public LoginResponse appleLogin(String token, String nickname) {

        // 애플 사용자 프로필 획득
        String uid = httpRequestService.getAppleUid(token);

        // 로그인 또는 회원가입 처리
        Member member = loginOrSignup(uid, nickname, null, LoginType.APPLE);
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid(), member.getLoginType());
        refreshTokenRedisService.saveRefreshToken(member.getUid(), refreshToken);
        return LoginResponse.of(MemberDto.from(member), accessToken, refreshToken);
    }

    /**
     * uid를 기반으로 회원이 존재할 시 로그인, 존재하지 않을 시 회원가입을 진행한다.
     *
     * @param uid       uid (카카오 또는 애플)
     * @param nickname  사용자 닉네임 (카카오 - 이름 / 애플 - GUEST)
     * @param loginType 소셜 로그인 타입
     * @return 로그인 (또는 회원가입) 완료 후의 Member Entity
     */
    private Member loginOrSignup(String uid, String nickname, String email, LoginType loginType) {
        return memberRepository.findByUidAndLoginTypeAndDeletedAtIsNull(uid, loginType)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .uid(uid)
                            .loginType(loginType)
                            .nickname(nickname)
                            .email(email)
                            .build();
                    memberRepository.save(newMember);
                    return newMember;
                });
    }

    /**
     * 회원 탈퇴 및 탈퇴 설문을 등록한다.
     *
     * @param memberId   탈퇴할 회원의 id
     * @param request  선택한 탈퇴 설문
     * @param identifier 카카오 - access token / 애플 - authorization code
     */
    @Transactional
    public void unlink(Long memberId, SurveyListRequest request, String identifier) {
        Member member = getMember(memberId);

        // 소셜 로그인 연결끊기
        if (member.getLoginType().equals(LoginType.KAKAO)) {
            httpRequestService.unlinkKakao(identifier);
        } else {
            httpRequestService.unlinkApple(identifier);
        }

        refreshTokenRedisService.deleteRefreshToken(member.getUid());
        memberRepository.delete(member);
        surveyRepository.saveAll(request.getData().stream().map(s -> s.toDto().toEntity(member)).toList());
    }

    /**
     * Authorization Header에 포함된 refresh token을 통해 access token 및 refresh token을 재발급한다.
     *
     * @param request Authorization Header(refresh token) 가 포함된 HttpServletRequest 객체
     * @return 재발급된 access token 및 refresh token
     */
    @Transactional
    public TokenResponse refresh(HttpServletRequest request) {
        String token = validateToken(request);
        LoginType loginType = tokenProvider.getLoginType(token);
        String uid = refreshTokenRedisService.findRefreshToken(token).getUid();
        String accessToken = tokenProvider.createAccessToken(uid, loginType);
        String refreshToken = tokenProvider.createRefreshToken(uid, loginType);
        refreshTokenRedisService.saveRefreshToken(uid, refreshToken);
        return TokenResponse.of(accessToken, refreshToken);
    }

    /**
     * HttpServletRequest에서 Token을 꺼내고 이를 검증한다.
     * 검증되지 않은 Token인 경우, Exception을 반환한다.
     * 검증되었을 경우, Token을 반환한다.
     *
     * @param request Http Request
     * @return request에서 검증이 완료된 JWT Token
     */
    private String validateToken(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        tokenProvider.validateToken(token);
        return token;
    }

    /**
     * memberId에 해당하는 사용자를 반환한다.
     * memberId에 해당하는 사용자가 존재하지 않거나 탈퇴한 사용자일 시, Exception을 던진다.
     *
     * @param memberId 찾고자 하는 사용자의 id
     * @return id에 해당하는 Member Entity
     */
    private Member getMember(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new MemberIdNotFoundException(memberId));
    }
}
