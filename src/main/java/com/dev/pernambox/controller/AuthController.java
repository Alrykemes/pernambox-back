package com.dev.pernambox.controller;

import com.dev.pernambox.domain.refreshToken.RefreshToken;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.AuthenticationException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.RefreshTokenService;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.utils.RequestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Validated
@Tag(name = "Auth", description = "Operações relacionadas a autenticação dos usuários")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(summary = "Realiza login do usuário", description = "Realiza login stateless do usuário a partir do email" +
            " e senha, retorna todas as informações do usuário")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = userService.getUserByEmail(body.email());

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais de Login Inválidas!");
        }

        String accessToken = jwtTokenService.generateToken(
                user,
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        UUID newToken = UUID.randomUUID();

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);

        System.out.println("Remember: " + body.rememberMe());
        if (body.rememberMe()) {
            newRefreshToken.setExpirationDate(LocalDateTime.now().plusDays(15));
        } else {
            newRefreshToken.setExpirationDate(LocalDateTime.now().plusHours(12));
        }

        try {
            RefreshToken refreshTokenFromDb = refreshTokenService.findByUserId(user.getId());
            if (body.rememberMe()) {
                refreshTokenService.updateById(refreshTokenFromDb.getToken(), newToken, LocalDateTime.now().plusDays(15));
            } else {
                refreshTokenService.updateById(refreshTokenFromDb.getToken(), newToken, LocalDateTime.now().plusHours(12));
            }
        } catch (NotFoundException e) {
            refreshTokenService.save(newRefreshToken);
            this.setRefreshTokenCookies(response, newRefreshToken.getToken(), newRefreshToken.getExpirationDate());
            return ResponseEntity.ok(new LoginResponseDto(user, accessToken));
        }

        this.setRefreshTokenCookies(response, newToken, newRefreshToken.getExpirationDate());
        return ResponseEntity.ok(new LoginResponseDto(user, accessToken));
    }

    @GetMapping("/me")
    @Operation(summary = "Verifica autenticação do usuário", description = "Verifica autenticação do usuário a partir da" +
            " jwt, e retorna todas as informações dele")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        if (authentication == null) throw new AuthenticationException("Authenticação é necessária nesse caminho!");

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "Atualiza o acess Token do usuário", description = "Atualiza o acess Token do usuário a partir " +
            "do cookie http-only se o RefreshToken estiver espirado retorna erro")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    System.out.println("Received refreshToken cookie: " + c.getName());
                }
            }
        } else {
            System.out.println("No cookies in request");
        }

        RefreshToken refreshTokenFromCookie = refreshTokenService.getRefreshTokenByCookies(request);

        RefreshToken refreshTokenFromDb = refreshTokenService.findByUserId(refreshTokenFromCookie.getUser().getId());

        if (!refreshTokenFromDb.getToken().equals(refreshTokenFromCookie.getToken())) {
            throw new AuthenticationException("O RefreshToken é inválido!");
        }

        if (refreshTokenFromDb.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("O RefreshToken está Expirado!");
        }

        UUID newToken = UUID.randomUUID();

        refreshTokenService.updateById(
                refreshTokenFromCookie.getToken(),
                newToken,
                refreshTokenFromDb.getExpirationDate()
        );

        this.setRefreshTokenCookies(response, newToken, refreshTokenFromDb.getExpirationDate());

        String accessToken = jwtTokenService.generateToken(
                refreshTokenFromCookie.getUser(),
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        return ResponseEntity.ok(
                new LoginResponseDto(
                        refreshTokenFromCookie.getUser().getId(),
                        refreshTokenFromCookie.getUser().getEmail(),
                        refreshTokenFromCookie.getUser().getRole(),
                        accessToken
                )
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Realiza logout do usuário", description = "Remove o refresh token do banco e limpa o cookie")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        RefreshToken refreshToken = refreshTokenService.getRefreshTokenByCookies(request);

        refreshTokenService.deleteByToken(refreshToken.getToken());

        this.setRefreshTokenCookies(response, null, LocalDateTime.now());

        return ResponseEntity.ok().build();
    }

    private void setRefreshTokenCookies(HttpServletResponse response, @Nullable UUID token, LocalDateTime expiration) {
        if (token == null) {
            Cookie cookie = new Cookie("refreshToken", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/auth");
            cookie.setMaxAge(0);
            cookie.setAttribute("SameSite", "Strict");
            response.addCookie(cookie);
            return;
        }

        Cookie cookie = new Cookie("refreshToken", token.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/auth");
        long maxAge = Duration.between(LocalDateTime.now(), expiration).getSeconds();
        cookie.setMaxAge(Math.toIntExact(maxAge));
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
}