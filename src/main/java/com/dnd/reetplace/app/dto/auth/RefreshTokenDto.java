package com.dnd.reetplace.app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshTokenDto {

    @Id
    private String uid;

    @Indexed
    private String refreshToken;
}
