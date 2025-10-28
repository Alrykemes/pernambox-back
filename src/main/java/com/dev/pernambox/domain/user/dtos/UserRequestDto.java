package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record UserRequestDto(
        @NotBlank(message = "nome é necessário")
        @Size(min = 6, max = 80, message = "nome deve te no minio 6 caracteres e no máximo 80")
        String name,
        @NotBlank(message = "nome é necessário")
        @Size(min = 6, max = 80, message = "cpf deve ter 11 caracteres")
        String cpf,
        @NotBlank(message = "email é necessário")
        @Email(message = "email precisa ser um email válido")
        String email,
        @NotBlank(message = "telefone é necessário")
        @Size(min = 6, max = 80, message = "telefone deve ter 11 caracteres")
        String phone,
        @NotNull(message = "role não pode ser nulo")
        Role role
) {
}