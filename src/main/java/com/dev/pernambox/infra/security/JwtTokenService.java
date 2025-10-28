package com.dev.pernambox.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("infra.security.jwt")
    private String secretKey;
    private final Map<String, Object> createPayload = new HashMap<>();
    private final Map<String, Claim> validatePayload = new HashMap<>();

    public String generateToken(User user, String ip, String userAgent) {
        if (user == null) throw new JWTCreationException("User not be a null", new Throwable());

        try {
            this.createPayload.clear();
            this.createPayload.put("userId", user.getId().toString());
            this.createPayload.put("email", user.getEmail());
            this.createPayload.put("role", user.getRole().toString());
            this.createPayload.put("ip", ip);
            this.createPayload.put("userAgent", userAgent);

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("login-auth")
                    .withSubject(user.getId().toString())
                    .withPayload(this.createPayload)
                    .withExpiresAt(this.generateExpirationDateMinutes())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new SecurityException("Error ao criar JWT de Login!");
        }
    }

    public Map<String, Claim> validateToken(String token, String ip, String userAgent) {
        try {
            this.validatePayload.clear();
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            this.validatePayload.clear();
            this.validatePayload.putAll(
                    JWT.require(algorithm)
                            .withIssuer("login-auth", "password-reset")
                            .build()
                            .verify(token)
                            .getClaims()
            );

            if (!this.validatePayload.get("ip").asString().equals(ip)) {
                log.warn("IP: {} \n User agent: {}, \n Tentando utilizar Jwt gerado por outro usuário", ip, userAgent);
                throw new SecurityException("Ip de requisição diferente do token");
            }

            if (!this.validatePayload.get("userAgent").asString().equals(userAgent)) {
                log.warn("IP: {} \n User agent: {}, \n Tentando utilizar Jwt gerado por outro usuário", ip, userAgent);
                throw new SecurityException("User Agent de requisição diferente do token");
            }

            return this.validatePayload;
        } catch (JWTVerificationException exception) {
            throw new SecurityException("Erro na Validação do token JWT");
        }
    }

    public String generatePasswordResetToken(UUID userId, String email, String ip, String userAgent) {

        try {
            this.createPayload.clear();
            this.createPayload.put("userId", userId.toString());
            this.createPayload.put("email", email);
            this.createPayload.put("ip", ip);
            this.createPayload.put("userAgent", userAgent);


            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("password-reset")
                    .withSubject(userId.toString())
                    .withPayload(this.createPayload)
                    .withExpiresAt(generateExpirationDateMinutes())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new SecurityException("Error ao criar JWT para Alterar senha!");
        }
    }

    private Instant generateExpirationDateMinutes() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
    }
}
