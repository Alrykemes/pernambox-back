package com.dev.pernambox.domain.dtos;

import com.dev.pernambox.domain.enums.Role;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoginResponseDto(UUID userID, String email, Role role, String token) {
}