package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRequestDto(
        @NotBlank(message = "nome é necessário")
        @Min(value = 6, message = "nome ter no mínimo 6 caracteres")
        @Max(value = 80, message = "nome ter no máximo 80 caracteres")
        String name,
        @NotBlank(message = "nome é necessário")
        @Min(value = 11, message = "cpf deve ter 11 caracteres")
        @Max(value = 11, message = "cpf deve ter 11 caracteres")
        String cpf,
        @NotBlank(message = "email é necessário")
        @Email(message = "email precisa ser um email válido")
        String email,
        @NotBlank(message = "telefone é necessário")
        @Min(value = 11, message = "telefone deve ter 11 caracteres")
        @Max(value = 11, message = "telefone deve ter 11 caracteres")
        String phone,
        @NotBlank(message = "role é necessário")
        Role role,
        @NotBlank(message = "unit_id é necessário")
        @org.hibernate.validator.constraints.UUID(message = "UUID precisa ser um UUIDv4 válido")
        UUID unit_Id
) {}