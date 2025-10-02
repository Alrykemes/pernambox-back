package com.dev.pernambox.domain.user.dtos;

import java.time.LocalTime;
import java.util.UUID;

public record OtpRecoveryPasswordResponseDto(
        Boolean sucess,
        UUID userId,
        LocalTime expiresIn) {}