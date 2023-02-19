package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.dto.auth.RefreshTokenDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenDto, String> {
    Optional<RefreshTokenDto> findByRefreshToken(String refreshToken);
}
