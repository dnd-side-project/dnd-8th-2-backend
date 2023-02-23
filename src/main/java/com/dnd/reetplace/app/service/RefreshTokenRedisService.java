package com.dnd.reetplace.app.service;

import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import com.dnd.reetplace.app.repository.RefreshTokenRedisRepository;
import com.dnd.reetplace.global.exception.auth.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RefreshTokenRedisService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public void saveRefreshToken(String uid, String refreshToken) {
        refreshTokenRedisRepository.save(new RefreshTokenDto(uid, refreshToken));
    }

    public RefreshTokenDto findRefreshToken(String refreshToken) {
        return refreshTokenRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    @Transactional
    public void deleteRefreshToken(String uid) {
        refreshTokenRedisRepository.deleteById(uid);
    }
}
