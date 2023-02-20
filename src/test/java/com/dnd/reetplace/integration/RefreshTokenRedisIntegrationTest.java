package com.dnd.reetplace.integration;

import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import com.dnd.reetplace.app.repository.RefreshTokenRedisRepository;
import com.dnd.reetplace.app.service.RefreshTokenRedisService;
import com.dnd.reetplace.global.exception.auth.RefreshTokenNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
public class RefreshTokenRedisIntegrationTest {

    @Autowired
    private RefreshTokenRedisService refreshTokenRedisService;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @DisplayName("refresh token 저장에 성공한다.")
    @Test
    void givenRefreshToken_whenSaveRefreshToken_thenSuccess() {

        // given
        String uid = "test_uid12345";
        String refreshToken = "testRefreshToken12345";

        // when
        refreshTokenRedisService.saveRefreshToken(uid, refreshToken);
        Optional<RefreshTokenDto> response = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        // then
        assertThat(response.isPresent()).isTrue();
        assertThat(response.get().getUid()).isEqualTo(uid);
        assertThat(response.get().getRefreshToken()).isEqualTo(refreshToken);
    }

    @DisplayName("refresh token 조회에 성공한다.")
    @Test
    void givenRefreshToken_whenFindRefreshToken_thenSuccess() {

        // given
        String uid = "test_uid12345";
        String refreshToken = "testRefreshToken12345";
        refreshTokenRedisRepository.save(new RefreshTokenDto(uid, refreshToken));

        // when
        RefreshTokenDto response = refreshTokenRedisService.findRefreshToken(refreshToken);

        // then
        assertThat(response.getUid()).isEqualTo(uid);
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    }

    @DisplayName("refresh token이 존재하지 않으면 조회에 실패한다.")
    @Test
    void givenRefreshToken_whenFindRefreshToken_thenTokenNotFoundFail() {

        // given
        String refreshToken = "testRefreshToken12345";

        // when & then
        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenRedisService.findRefreshToken(refreshToken));
    }

}
