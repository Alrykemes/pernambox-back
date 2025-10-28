package com.dev.pernambox.service;

import com.dev.pernambox.exceptions.PasswordResetException;
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
        String key = "otp:" + userId.toString();
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(15));
        return otp;
    }

    public void validateOtp(UUID userId, String otpCode) throws PasswordResetException {
        String key = "otp:" + userId.toString();
        String otpCodeRedis = redisTemplate.opsForValue().get(key);

        if (otpCode.equals(otpCodeRedis)) {
            redisTemplate.delete(key);
        } else {
            throw new PasswordResetException("Código Inválido ou expirado!");
        }
    }

    public void insertPasswordResetToken(UUID userId, String token) {
        String key = "password_reset:" + userId.toString();
        redisTemplate.opsForValue().set(key, token, Duration.ofMinutes(15));
    }

    public void validatePasswordResetToken(UUID userId, String token) {
        String key = "password_reset:" + userId.toString();
        String tokenRedis = redisTemplate.opsForValue().get(key);

        if (token.equals(tokenRedis)) {
            redisTemplate.delete(key);
        } else {
            throw new PasswordResetException("Token Inválido ou Expirado!");
        }
    }
}
