package com.dev.pernambox.controller;

import com.dev.pernambox.domain.User;
import com.dev.pernambox.domain.dtos.LoginRequestDto;
import com.dev.pernambox.domain.dtos.LoginResponseDto;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.repositories.UserRepository;
import com.dev.pernambox.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto body, HttpServletRequest request) {
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.senha(), user.getPassword())) {
            String token = this.jwtTokenService.gerarToken(
                    user,
                    RequestUtils.getRequestIp(request),
                    RequestUtils.getRequestUserAgent(request)
            );

            return ResponseEntity.ok(new LoginResponseDto(user.getId(), user.getEmail(), user.getPermissao(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    // Futuramente retorar um Dto do User aqui quando criar o CRUD de user
    @GetMapping("/me")
    public ResponseEntity<LoginResponseDto> me(Authentication authentication, HttpServletRequest request) {
        if (authentication == null) return ResponseEntity.badRequest().build();

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getPermissao(),
                request.getHeader("Authorization").toString().replace("Bearer ", "")));
    }
}