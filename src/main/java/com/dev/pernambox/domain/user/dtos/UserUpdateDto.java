package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record UserUpdateDto(

        @NotBlank(message = "userId é necessário")
        @org.hibernate.validator.constraints.UUID(message = "userId precisa ser um UUIDv4 válido")
        UUID userId,
        @NotBlank(message = "name é necessário")
        @Min(value = 6, message = "name ter no mínimo 6 caracteres")
        @Max(value = 80, message = "name ter no máximo 80 caracteres")
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
        @NotBlank(message = "Password é necessário")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*`?&.#^'_-]{8,}$",
                message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial, com no mínimo 8 caracteres."
        )
        @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
        String password,
        @NotBlank(message = "unitId é necessário")
        @org.hibernate.validator.constraints.UUID(message = "unitId precisa ser um UUIDv4 válido")
        UUID unitId,
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*`?&.#^'_-]{8,}$",
                message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial, com no mínimo 8 caracteres."
        )
        @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
        String newPassword
) {}