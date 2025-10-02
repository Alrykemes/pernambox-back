package com.dev.pernambox.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;


public record RecoveryPasswordRequestDto(
        @NotBlank(message = "userId is required")
        @UUID(message = "userId must be a valid UUID")
        java.util.UUID userId,
        @NotBlank(message = "password is required")
        @Size(min = 8, message = "password must be at least 8 characters")
        String password,
        @NotBlank(message = "otpCode is required")
        @Size(min =6, max = 6, message = "OTP require exactly 6 characters")
        String otpCode
) {}
