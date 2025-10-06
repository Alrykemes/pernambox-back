package com.dev.pernambox.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewPasswordRequestDto(
        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*`?&.#^'_-]{8,}$",
                message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial, com no mínimo 8 caracteres."
        )
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password
) {}


