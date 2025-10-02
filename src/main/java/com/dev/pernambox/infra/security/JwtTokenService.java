package com.dev.pernambox.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("infra.security.jwt")
    private String secretKey;
    private Map<String, Object> payload = new HashMap<>();

    public String generateToken(User user, String ip, String userAgent) {
        if(user == null) throw new JWTCreationException("User not be a null", new Throwable());

        try {
            payload.put("userId", user.getId().toString());
            payload.put("email", user.getEmail());
            payload.put("role", user.getRole().toString());
            payload.put("unitId", user.getUnit().getId().toString());
            payload.put("ip", ip);
            payload.put("userAgent", userAgent);

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("login-auth")
                    .withSubject(user.getId().toString())
                    .withPayload(this.payload)
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException ex) {
            throw new SecurityException();
        }
    }

    public Map<String, Claim> validateToken(String token, String ip, String userAgent) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            Map<String, Claim> payload = JWT.require(algorithm)
                    .withIssuer("login-auth")
                    .build()
                    .verify(token)
                    .getClaims();

            if (!payload.get("ip").asString().equals(ip)) {
                // guarda no log
                throw new SecurityException("Ip de requisição diferente do token");
            }

            if (!payload.get("userAgent").asString().equals(userAgent)) {
                // guarda no log
                throw new SecurityException("User Agent de requisição diferente do token");
            }

            return payload;
        } catch (JWTVerificationException exception) {
            // guarda no log
            throw new SecurityException();
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-08:00"));
    }
}
