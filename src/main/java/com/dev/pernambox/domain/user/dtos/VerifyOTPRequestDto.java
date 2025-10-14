package com.dev.pernambox.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VerifyOTPRequestDto(
        @NotBlank(message = "otpCode is required")
        @Pattern(regexp = "\\d{6}", message = "otpCode must contain exactly 6 digits")
        String otpCode,

        @NotNull(message = "userId is required")
        java.util.UUID userId
) {}
