package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.PasswordResetException;
import com.dev.pernambox.infra.security.JwtTokenService;
import com.dev.pernambox.service.EmailService;
import com.dev.pernambox.service.RedisService;
import com.dev.pernambox.service.UserService;
import com.dev.pernambox.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final RedisService redisService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<PasswordResetOtpResponseDto> initiatePasswordReset(
            @RequestParam
            @NotBlank(message = "email is required")
            @Email(message = "email must be a valid email")
            String email
    ) {
        User user = userService.getUserByEmail(email);
        String otpCode = redisService.generateOtpCode(user.getId());
        emailService.sendEmail(email, "Pernambox - Recuperação Senha", "Seu código de recuperação de senha: " + otpCode);
        return ResponseEntity.ok(new PasswordResetOtpResponseDto(true, user.getId(), LocalTime.now().plusMinutes(15)));
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<VerifyOTPResponseDto> verifyOTP(@RequestBody VerifyOTPRequestDto body, HttpServletRequest request) {
        if (!redisService.validateOtp(body.userId(), body.otpCode())) {
            throw new PasswordResetException("Invalid or expired OTP code");
        }

        User user = userService.getUserById(body.userId().toString());

        String token = jwtTokenService.generatePasswordResetToken(
                body.userId(),
                user.getEmail(),
                RequestUtils.getRequestIp(request),
                RequestUtils.getRequestUserAgent(request)
        );

        return ResponseEntity.ok(new VerifyOTPResponseDto(true, token));
    }

    @PatchMapping()
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NewPasswordRequestDto body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (userService.changeUserPassword(user.getId(), body.password())) {
            return ResponseEntity.ok().build();
        } else {
            throw new PasswordResetException("Failed to reset password in DB");
        }
    }

}
