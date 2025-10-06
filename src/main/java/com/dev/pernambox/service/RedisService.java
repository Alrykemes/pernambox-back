package com.dev.pernambox.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final Random random = new Random();

    public String generateOtpCode(UUID userId) {
        String otp = String.valueOf(100000 + random.nextInt(900000));
        String key = "otp:" + otp;
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(15));
        redisTemplate.opsForValue().set(key, userId.toString(), Duration.ofMinutes(15));
        return otp;
    }

    public UUID validateOtpAndGetUserId(String otpCode) {
        String key = "otp:" + otpCode;
        String userIdStr = redisTemplate.opsForValue().get(key);

        if (userIdStr != null) {
            redisTemplate.delete(key);
            return UUID.fromString(userIdStr);
        }
        return null;
    }
}
