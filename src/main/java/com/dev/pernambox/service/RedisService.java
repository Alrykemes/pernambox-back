package com.dev.pernambox.service;

import com.dev.pernambox.exceptions.PasswordResetException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
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

    public Boolean validateOtp(UUID userId, String otpCode) throws PasswordResetException {
        String key = "otp:" + userId.toString();
        String otpCodeRedis = redisTemplate.opsForValue().get(key);

        if (otpCode.equals(otpCodeRedis)) {
            redisTemplate.delete(key);
            return true;
        } else {
            throw new PasswordResetException("OTP code is invalid or expired");
        }
    }
}
