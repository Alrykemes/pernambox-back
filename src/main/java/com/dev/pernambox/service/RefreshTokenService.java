package com.dev.pernambox.service;

import com.dev.pernambox.domain.refreshToken.RefreshToken;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.repositories.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByToken(UUID token) throws NotFoundException {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found"));
    }

    public RefreshToken findByUserId(UUID userId) throws NotFoundException {
        return refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found"));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean updateById(UUID token, UUID newToken, LocalDateTime expirationDate) throws UpdateEntityException {
        return refreshTokenRepository.updateRefreshByToken(
                token,
                newToken,
                expirationDate
        ) < 0;
    }

    public RefreshToken getRefreshTokenByCookies(HttpServletRequest request) throws NotFoundException {
        if(request.getCookies() == null) {
            throw new NotFoundException("Refresh Token Not Found in Cookies");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(cookie -> {
                    String refreshToken = cookie.getValue();
                    return this.findByToken(UUID.fromString(refreshToken));
                })
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found in Cookies"));
    }
}
