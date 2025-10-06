package com.dev.pernambox.domain.user.dtos;

import java.time.LocalTime;
import java.util.UUID;

public record PasswordResetOtpResponseDto(boolean success, UUID userId, LocalTime expiresIn) {
}