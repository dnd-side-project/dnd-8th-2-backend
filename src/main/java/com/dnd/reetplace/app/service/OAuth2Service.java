package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import com.dnd.reetplace.app.dto.auth.response.KakaoProfileResponse;
import com.dnd.reetplace.app.dto.auth.response.LoginResponse;
import com.dnd.reetplace.app.dto.auth.response.TokenResponse;
import com.dnd.reetplace.app.dto.member.MemberDto;
import com.dnd.reetplace.app.dto.survey.SurveyDto;
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

        KakaoProfileResponse kakaoProfile = httpRequestService.getKakaoProfile(token);

        // 로그인 또는 회원가입 처리
        Member member = kakaoLoginOrSignup(kakaoProfile);
        String accessToken = tokenProvider.createAccessToken(member.getUid(), member.getLoginType());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid(), member.getLoginType());
        refreshTokenRedisService.saveRefreshToken(member.getUid(), refreshToken);
        return LoginResponse.of(MemberDto.from(member), accessToken, refreshToken);
    }

    /**
     * 카카오 서버에서 받은 사용자 프로필을 기반으로 회원이 존재할 시 로그인, 존재하지 않을 시 회원가입을 진행한다.
     *
     * @param kakaoProfile 사용자 카카오 프로필
     * @return 로그인 (또는 회원가입) 완료 후의 Member Entity
     */
    private Member kakaoLoginOrSignup(KakaoProfileResponse kakaoProfile) {
        String uid = kakaoProfile.getId().toString();
        return memberRepository.findByUidAndLoginTypeAndDeletedAtIsNull(uid, LoginType.KAKAO)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .uid(uid)
                            .loginType(LoginType.KAKAO)
                            .nickname(kakaoProfile.getNickname())
                            .build();
                    memberRepository.save(newMember);
                    return newMember;
                });
    }

    /**
     * 회원 탈퇴 및 탈퇴 설문을 등록한다.
     *
     * @param memberId         탈퇴할 회원의 id
     * @param surveyDto        탈퇴 설문 관련 dto
     * @param kakaoAccessToken 카카오 access token
     */
    @Transactional
    public void unlink(Long memberId, SurveyDto surveyDto, String kakaoAccessToken) {
        Member member = getMember(memberId);

        // 소셜 로그인 연결끊기
        if (member.getLoginType().equals(LoginType.KAKAO)) {
            httpRequestService.unlinkKakao(kakaoAccessToken);
        } // TODO Apple login 구현 시 Apple login에 대한 unlink 로직 구현

        refreshTokenRedisService.deleteRefreshToken(member.getUid());
        memberRepository.delete(member);
        surveyRepository.save(surveyDto.toEntity(member));
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
