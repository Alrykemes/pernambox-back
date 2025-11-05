package com.dev.pernambox.domain.user.dtos;

import com.dev.pernambox.domain.user.enums.Role;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record UserUpdateDto(

        @NotNull(message = "userId é necessário")
        UUID userId,
        @Size(min = 6, max = 80, message = "name deve ter no mínimo 6 caracteres e no máximo 80 caracteres!")
        String name,
        @Size(min = 11, max = 11, message = "cpf deve ter 11 caracteres!")
        String cpf,
        @Email(message = "email precisa ser um email válido")
        String email,
        @Size(min = 11, max = 11, message = "cpf deve ter 11 caracteres!")
        String phone,
        Role role,
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*`?&.#^'_-]{8,}$",
                message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial, com no mínimo 8 caracteres."
        )
        @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
        String password,
        Boolean active,
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*`?&.#^'_-]{8,}$",
                message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial, com no mínimo 8 caracteres."
        )
        @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
        String newPassword
) {}