package com.dev.pernambox.domain.user.dtos;

import java.time.LocalTime;
import java.util.UUID;

public record InitiatePasswordResetResponseDto(boolean success, UUID userId, LocalTime expiresIn) {}