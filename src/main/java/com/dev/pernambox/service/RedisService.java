package com.dev.pernambox.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@AllArgsConstructor
@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final Random random = new Random();

    public String gerarOtp(String userId) {
        String otp = String.valueOf(100000 + random.nextInt(900000));

        String chave = "otp:" + userId;
        redisTemplate.opsForValue().set(chave, otp, Duration.ofMinutes(15));

        return otp;
    }

    public boolean validarOtp(String userId, String otp) {
        String chave = "otp:" + userId;
        String valor = redisTemplate.opsForValue().get(chave);

        if (valor != null && valor.equals(otp)) {
            redisTemplate.delete(chave);
            return true;
        }
        return false;
    }
}
