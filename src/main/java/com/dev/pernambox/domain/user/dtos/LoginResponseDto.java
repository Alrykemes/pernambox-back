package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.enums.Role;

import java.util.UUID;

public record LoginResponseDto(UUID userId, String email, Role role, String token) {
    public LoginResponseDto(User user, String accessToken) {
        this(user.getId(), user.getEmail(), user.getRole(), accessToken);
    }
}