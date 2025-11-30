package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.domain.user.User;

import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String name,
        String cpf,
        String email,
        String phone,
        Boolean active,
        String imageProfileName,
        Role role
) {
    public UserResponseDto(User user) {
        this(user.getId(), user.getName(), user.getCpf(), user.getEmail(), user.getPhone(), user.getActive(), user.getImageProfile(), user.getRole());
    }
}