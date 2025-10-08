package com.dev.pernambox.domain.refreshToken.dtos;

import com.dev.pernambox.domain.refreshToken.RefreshToken;

import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshTokenDto(
   String acessToken,
   UUID token,
   UUID userId,
   LocalDateTime expirationDate
) {
    public RefreshTokenDto(RefreshToken refreshToken, String jwt) {
        this(jwt, refreshToken.getToken(), refreshToken.getUser().getId(), refreshToken.getExpirationDate());
    }
}