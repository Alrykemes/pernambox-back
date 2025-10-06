package com.dev.pernambox.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;

public record VerifyOTPRequestDto(
        @NotBlank(message = "otpCode is required")
        @Pattern(regexp = "\\d{6}", message = "otpCode must contain exactly 6 digits")
        String otpCode,
        @NotBlank(message = "userId is required")
        @UUID(message = "userId must be a UUID")
        java.util.UUID userId
) {}
