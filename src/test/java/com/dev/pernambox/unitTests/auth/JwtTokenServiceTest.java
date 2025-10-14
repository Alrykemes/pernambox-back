package com.dev.pernambox.unitTests.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.domain.address.Address;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class JwtTokenServiceTest {
//
//    private JwtTokenService jwtTokenService;
//    private String tokenValido;
//    private User userTest;
//
//    @BeforeEach
//    public void setup() {
//        this.userTest = new User(UUID.randomUUID(),
//                "teste",
//                "teste",
//                "teste",
//                "teste",
//                "teste",
//                Role.USER,
//                new Unit(UUID.randomUUID(),
//                        "name of Unit",
//                        new Address(UUID.randomUUID(), "", "", "", "", "", "", ""),
//                        new ArrayList<User>()));
//
//        String ip = "192.168.0.1";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
//
//        this.jwtTokenService = new JwtTokenService();
//        ReflectionTestUtils.setField(this.jwtTokenService, "secretKey", "secretkeyteste123");
//
//        RefreshTokenDto refreshTokenDto = jwtTokenService.generateTokens(this.userTest, ip, userAgent);
//        this.tokenValido = refreshTokenDto.acessToken();
//    }
//
//    @Test
//    public void deveCriarTokenComSucesso() {
//        String ip = "192.168.0.1";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
//
//        RefreshTokenDto refreshTokenDto = jwtTokenService.generateTokens(this.userTest, ip, userAgent);
//        this.tokenValido = refreshTokenDto.acessToken();
//
//        assertNotNull("Valida se token é nulo", tokenValido);
//
//        String[] partes = this.tokenValido.split("\\.");
//
//        // Um JWT válido tem 3 partes
//        assertEquals("Valida se token JWT contem 3 partes", 3, partes.length);
//    }
//
//    @Test
//    public void deveFalharCriarTokenSeUserNulo() {
//        String ip = "192.168.0.2";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
//
//        assertThrows(JWTCreationException.class, () -> {
//                    RefreshTokenDto refreshTokenDto = jwtTokenService.generateTokens(null, ip, userAgent);
//                    this.tokenValido = refreshTokenDto.acessToken();
//                }
//        );
//    }
//
//    @Test
//    public void deveValidarTokenComSucesso() {
//        String ip = "192.168.0.1";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
//
//        Map<String, Claim> claims = jwtTokenService.validateToken(tokenValido, ip, userAgent);
//
//        assertNotNull("payload do token", claims);
//        assertEquals("valida ip", ip, claims.get("ip").asString());
//        assertEquals("valida userAgent", userAgent, claims.get("userAgent").asString());
//    }
//
//    @Test
//    public void deveFalharSeIpIncorreto() {
//        String ip = "192.168.0.2";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";
//
//        assertThrows(SecurityException.class, () -> {
//                    Map<String, Claim> claims = jwtTokenService.validateToken(tokenValido, ip, userAgent);
//                }
//        );
//    }
//
//    @Test
//    public void deveFalharSeUserAgentIncorreto() {
//        String ip = "192.168.0.1";
//        String userAgent = "Mozilla/4.0 (Windows NT 10.0; Win64; x64)";
//
//        assertThrows(SecurityException.class, () -> {
//                    Map<String, Claim> claims = jwtTokenService.validateToken(tokenValido, ip, userAgent);
//                }
//        );
//    }

}