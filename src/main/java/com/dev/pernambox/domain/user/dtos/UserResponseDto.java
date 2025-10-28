package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.domain.user.User;

import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String name,
        String cpf,
        String email,
        String phone,
        Role role
) {
    public UserResponseDto(User user) {
        this(user.getId(), user.getName(), user.getCpf(), user.getEmail(), user.getPhone(), user.getRole());
    }
}