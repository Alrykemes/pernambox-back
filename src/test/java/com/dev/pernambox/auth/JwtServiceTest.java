package com.dev.pernambox.auth;

import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.domain.Polo;
import com.dev.pernambox.domain.User;
import com.dev.pernambox.domain.enums.Permissao;
import com.dev.pernambox.infra.security.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class JwtServiceTest {

    private JwtTokenService jwtTokenService;
    private String tokenValido;
    private String tokenInvalido;

    @Value("infra.security.jwt")
    private String secretKey;

    @BeforeEach
    public void setup() {
        User user = new User(UUID.randomUUID(),
                "teste",
                "teste",
                "teste",
                "teste",
                "teste",
                Permissao.FUNCIONARIO,
                new Polo(UUID.randomUUID(), "", "", "", "", "", new ArrayList<User>()));

        String ip = "192.168.0.1";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        this.tokenValido = jwtTokenService.gerarToken(user, ip, userAgent);
    }

    @Test
    public void deveCriarTokenComSucesso() {
        String ip = "192.168.0.2";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);

        assertNotNull("payload do token", claims);
    }

    @Test
    public void deveFalharCriarTokenSeUserNulo() {
        String ip = "192.168.0.2";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);

        assertNotNull("payload do token", claims);
    }

    @Test
    public void deveValidarTokenComSucesso() {
        String ip = "192.168.0.1";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);

        assertNotNull("payload do token", claims);
        assertEquals("valida ip", ip, claims.get("ip").asString());
        assertEquals("valida userAgent", userAgent, claims.get("userAgent").asString());
    }

    @Test
    public void deveFalharSeIpIncorreto() {
        String ip = "192.168.0.2";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);

        assertNotNull("payload do token", claims);
    }

    @Test
    public void deveFalharSeUserAgentIncorreto() {
        String ip = "192.168.0.1";
        String userAgent = "Mozilla/4.0 (Windows NT 10.0; Win64; x64)";

        assertThrows(SecurityException.class, () -> {
                    Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);
                }
        );
    }

}