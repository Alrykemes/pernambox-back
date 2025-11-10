package com.dev.pernambox.unitTests.auth;

import com.dev.pernambox.controller.AuthController;
import com.dev.pernambox.domain.user.dtos.LoginRequestDto;
import com.dev.pernambox.domain.user.dtos.LoginResponseDto;
import com.dev.pernambox.exceptions.handlers.GlobalExceptionHandler;
import com.dev.pernambox.exceptions.AuthenticationException;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.refreshToken.RefreshToken;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler()) // se existir
                .build();
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        // Arrange
        LoginRequestDto requestDto = new LoginRequestDto("user@email.com", "12345678", true);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@email.com");
        user.setPassword("encodedPassword");

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenService.generateToken(any(), any(), any())).thenReturn("mockedAccessToken");
        when(refreshTokenService.findByUserId(any())).thenThrow(new com.dev.pernambox.exceptions.NotFoundException("Token not found"));
        doNothing().when(refreshTokenService).save(any(RefreshToken.class));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.token").value("mockedAccessToken"));
    }

    @Test
    void deveFalharQuandoSenhaIncorreta() throws Exception {
        // Arrange
        LoginRequestDto requestDto = new LoginRequestDto("user@email.com", "senhaErrada", false);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@email.com");
        user.setPassword("encodedPassword");

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthenticationException)
                )

                .andExpect(result ->
                        result.getResolvedException().getMessage().equals("Credenciais de Login Inválidas!")
                );
    }

}
