package com.dev.pernambox.domain.product.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefProductRequestDto(
        @NotBlank(message = "O gtin é necessário")
        @Size(min = 14,max = 14, message = "O gtin deve ter 14 caracteres")
        String gtin,
        @NotBlank(message = "A descrição é necessária")
        String description,
        @NotNull(message = "O preço médio é necessário")
        Float avg_price,
        @NotBlank(message = "A marca é necessária")
        String brand,
        @NotBlank(message = "A imagem é necessária")
        String image
) {}
