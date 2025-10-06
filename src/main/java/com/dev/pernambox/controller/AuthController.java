package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.AuthenticationException;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.EmailService;
import com.dev.pernambox.service.RedisService;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final RedisService redisService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto body,
            HttpServletRequest request
    ) {
        User user = userService.getUserByEmail(body.email());

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String token = jwtTokenService.generateToken(
                user,
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        return ResponseEntity.ok(new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        ));
    }

@GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        if (authentication == null) throw new AuthenticationException("Auth Required to this Path");

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserResponseDto(user));
    }
}