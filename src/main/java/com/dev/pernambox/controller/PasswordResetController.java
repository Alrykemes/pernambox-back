package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.PasswordResetException;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.EmailService;
import com.dev.pernambox.service.RedisService;
import com.dev.pernambox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final RedisService redisService;
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<PasswordResetOtpResponseDto> initiatePasswordReset(@RequestBody PasswordResetOtpRequestDto body) {
        User user = userService.getUserByEmail(body.email());
        String otpCode = redisService.generateOtpCode(user.getId());
        emailService.sendEmail(body.email(), "Pernambox - Recuperação Senha", "Seu código de recuperação de senha: " + otpCode);
        return ResponseEntity.ok(new PasswordResetOtpResponseDto(true, user.getId(), LocalTime.now().plusMinutes(15)));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyOTPResponseDto> verifyOTP(@RequestBody VerifyOTPRequestDto body) {
        UUID userId = redisService.validateOtpAndGetUserId(body.otpCode());

        if (userId == null) {
            throw new PasswordResetException("Invalid or expired OTP code");
        }

        String token = jwtTokenService.generatePasswordResetToken(userId);
        return ResponseEntity.ok(new VerifyOTPResponseDto(true, token));
    }

    @PatchMapping("")
    public ResponseEntity<NewPasswordResponseDto> resetPassword(@RequestBody NewPasswordRequestDto body, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtTokenService.validatePasswordResetToken(token);
        userService.changeUserPassword(userId, passwordEncoder.encode(body.password()));
        return ResponseEntity.ok(new NewPasswordResponseDto(true));
    }

}
