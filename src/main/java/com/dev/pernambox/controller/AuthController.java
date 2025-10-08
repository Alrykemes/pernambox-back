package com.dev.pernambox.controller;

import com.dev.pernambox.domain.refreshToken.RefreshToken;
import com.dev.pernambox.domain.refreshToken.dtos.RefreshTokenDto;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.AuthenticationException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.RefreshTokenService;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.utils.RequestUtils;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = userService.getUserByEmail(body.email());

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String acessToken = jwtTokenService.generateToken(
                user,
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setExpirationDate(LocalDateTime.now().plusDays(15));

        UUID newToken = UUID.randomUUID();

        try {
            RefreshToken refreshTokenFromDb = refreshTokenService.findByUserId(user.getId());
            refreshTokenService.updateById(refreshTokenFromDb.getToken(), newToken, LocalDateTime.now().plusDays(15));
        } catch (NotFoundException e) {
            refreshTokenService.save(newRefreshToken);
            this.setRefreshTokenCookies(response, newRefreshToken.getToken());
            return ResponseEntity.ok(new LoginResponseDto(user, acessToken));
        }

        this.setRefreshTokenCookies(response, newToken);
        return ResponseEntity.ok(new LoginResponseDto(user, acessToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        if (authentication == null) throw new AuthenticationException("Auth Required to this Path");

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        RefreshToken refreshTokenFromCookie = refreshTokenService.getRefreshTokenByCookies(request);

        RefreshToken refreshTokenFromDb = refreshTokenService.findByUserId(refreshTokenFromCookie.getUser().getId());

        if (!refreshTokenFromDb.getToken().equals(refreshTokenFromCookie.getToken())) {
            throw new AuthenticationException("RefreshToken is Invalid!");
        }

        if (refreshTokenFromDb.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("RefreshToken is Expired!");
        }

        UUID newToken = UUID.randomUUID();

        refreshTokenService.updateById(
                refreshTokenFromCookie.getToken(),
                newToken,
                LocalDateTime.now().plusDays(15)
        );

        this.setRefreshTokenCookies(response, newToken);

        String acessToken = jwtTokenService.generateToken(
                refreshTokenFromCookie.getUser(),
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        return ResponseEntity.ok(
                new LoginResponseDto(
                        refreshTokenFromCookie.getUser().getId(),
                        refreshTokenFromCookie.getUser().getEmail(),
                        refreshTokenFromCookie.getUser().getRole(),
                        acessToken
                )
        );
    }

    private void setRefreshTokenCookies(HttpServletResponse response, UUID token) {
        Cookie cookie = new Cookie("refreshToken", token.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh-token");
        cookie.setMaxAge(15 * 60 * 24 * 30);
        response.addCookie(cookie);
    }
}