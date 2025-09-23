package com.dev.pernambox.domain.dtos;

import com.dev.pernambox.domain.enums.Permissao;

import java.util.UUID;

public record LoginResponseDto(UUID userID, String email, Permissao permissao, String token) {
}