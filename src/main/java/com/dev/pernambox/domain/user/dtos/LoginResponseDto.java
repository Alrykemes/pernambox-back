package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;

import java.util.UUID;

public record LoginResponseDto(UUID userId, String email, Role role, String token) {}