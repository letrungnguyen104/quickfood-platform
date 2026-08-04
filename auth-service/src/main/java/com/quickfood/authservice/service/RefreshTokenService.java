package com.quickfood.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    @Value("${app.jwt.refresh-expiration-in-ms}")
    private long refreshExpiration;

    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        
        redisTemplate.opsForValue().set(
                refreshToken, 
                username, 
                Duration.ofMillis(refreshExpiration)
        );
        
        return refreshToken;
    }

    public String getUsernameFromRefreshToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete(token);
    }
}