package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.repositories.UserRepository;
import com.dev.pernambox.service.EmailService;
import com.dev.pernambox.service.RedisService;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final RedisService redisService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto body, HttpServletRequest request) {
        User user = this.userService.getUserByEmail(body.email());
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.jwtTokenService.generateToken(
                    user,
                    RequestUtils.getRequestIp(request),
                    RequestUtils.getRequestUserAgent(request)
            );

            return ResponseEntity.ok(new LoginResponseDto(user.getId(), user.getEmail(), user.getRole(), token));
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
                user.getRole(),
                request.getHeader("Authorization").toString().replace("Bearer ", "")));
    }

    @GetMapping("/recoveryPassword/sendOtp")
    public ResponseEntity<OtpRecoveryPasswordResponseDto> sendOtpEmail(@RequestBody OtpRecoveryPasswordRequestDto body) {
        try {
            User user = userService.getUserByEmail(body.email());
            emailService.sendEmail(body.email(),
                    "Recuperação Senha - Pernambox",
                    "Olá, Aqui esta o seu código de recuperação de senha: " + redisService.gerarOtp(user.getId().toString()));

            return ResponseEntity.ok(new OtpRecoveryPasswordResponseDto(true, user.getId(), LocalTime.now().plusMinutes(15)));
        } catch (Exception e) {
            // log E
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recoveryPassword/resetPassword")
    public ResponseEntity<?> sendOtpEmail(@RequestBody RecoveryPasswordRequestDto body) {
        if (redisService.validarOtp(body.userId().toString(), body.otpCode())) {
            User user = userService.changeUserPassword(body.userId(), body.password());
            return ResponseEntity.ok(new UserResponseDto(user));
        }

        return ResponseEntity.ok().build();
    }
}