package com.dev.pernambox.domain.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetOtpRequestDto(
        @NotBlank(message = "email is required") @Email(message = "email must be a valid email") String email) {
}