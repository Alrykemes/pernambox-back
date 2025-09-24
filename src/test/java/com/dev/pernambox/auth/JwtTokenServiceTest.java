package com.dev.pernambox.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.domain.Polo;
import com.dev.pernambox.domain.User;
import com.dev.pernambox.domain.enums.Permissao;
import com.dev.pernambox.infra.security.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;
    private String tokenValido;
    private User userTest;

    @BeforeEach
    public void setup() {
        this.userTest = new User(UUID.randomUUID(),
                "teste",
                "teste",
                "teste",
                "teste",
                "teste",
                Permissao.FUNCIONARIO,
                new Polo(UUID.randomUUID(), "", "", "", "", "", new ArrayList<User>()));

        String ip = "192.168.0.1";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        this.jwtTokenService = new JwtTokenService();
        ReflectionTestUtils.setField(this.jwtTokenService, "secretKey", "secretkeyteste123");

        this.tokenValido = jwtTokenService.gerarToken(this.userTest, ip, userAgent);
    }

    @Test
    public void deveCriarTokenComSucesso() {
        String ip = "192.168.0.1";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        this.tokenValido = jwtTokenService.gerarToken(this.userTest, ip, userAgent);

        assertNotNull("Valida se token é nulo", tokenValido);

        String[] partes = this.tokenValido.split("\\.");

        // Um JWT válido tem 3 partes
        assertEquals("Valida se token JWT contem 3 partes",3, partes.length);
    }

    @Test
    public void deveFalharCriarTokenSeUserNulo() {
        String ip = "192.168.0.2";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)";

        assertThrows(JWTCreationException.class, () -> {
                    this.tokenValido = jwtTokenService.gerarToken(null, ip, userAgent);
                }
        );
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

        assertThrows(SecurityException.class, () -> {
                    Map<String, Claim> claims = jwtTokenService.validarToken(tokenValido, ip, userAgent);
                }
        );
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