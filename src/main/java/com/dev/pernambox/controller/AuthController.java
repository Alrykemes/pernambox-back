package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.exceptions.AuthException;
import com.dev.pernambox.exceptions.RecoveryPasswordException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto body, HttpServletRequest request) throws AuthException {
        try {
            User user = this.userService.getUserByEmail(body.email());
            if (passwordEncoder.matches(body.password(), user.getPassword())) {
                String token = this.jwtTokenService.generateToken(
                        user,
                        RequestUtils.getRequestIp(request),
                        RequestUtils.getRequestUserAgent(request)
                );

                return ResponseEntity.ok(new LoginResponseDto(user.getId(), user.getEmail(), user.getRole(), token));
            } else {
                throw new AuthException("Incorrect Password! Try again later or contact the administrator of the system!");
            }
        } catch (Exception e) {
            // log exception error
            e.printStackTrace();
            throw new AuthException(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) throws AuthException {
        if (authentication == null) throw new AuthException("Auth Required to this Path");

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("/recoveryPassword/sendOtp")
    public ResponseEntity<OtpRecoveryPasswordResponseDto> sendOtpEmail(
            @RequestParam
            @NotBlank(message = "Param email is required")
            @Email(message = "Param email must be a valid email")
            String email) throws RecoveryPasswordException {
        try {
            User user = userService.getUserByEmail(email);
            emailService.sendEmail(email,
                    "Recuperação Senha - Pernambox",
                    "Olá, Aqui esta o seu código de recuperação de senha: " + redisService.gerarOtp(user.getId().toString())
            );
            return ResponseEntity.ok(new OtpRecoveryPasswordResponseDto(true, user.getId(), LocalTime.now().plusMinutes(15)));
        } catch (Exception e) {
            // log exception error
            e.printStackTrace();
            throw new RecoveryPasswordException("Error in send email for recovery password! Try again later or contact the administrator of the system!");
        }
    }

    @PatchMapping("/recoveryPassword/resetPassword")
    public ResponseEntity<?> sendOtpEmail(@Valid @RequestBody RecoveryPasswordRequestDto body) throws RecoveryPasswordException {
        if (redisService.validarOtp(body.userId().toString(), body.otpCode())) {
            if (userService.changeUserPassword(body.userId(), body.password())) {
                return ResponseEntity.ok().build();
            } else {
                throw new RecoveryPasswordException("Error in update user password!");
            }
        } else {
            throw new RecoveryPasswordException("Incorrect OTP CODE!");
        }
    }
}