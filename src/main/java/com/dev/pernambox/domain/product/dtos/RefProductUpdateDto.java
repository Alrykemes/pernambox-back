package com.dev.pernambox.domain.product.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RefProductUpdateDto(
        @NotNull(message = "O id do produto base é necessário")
        UUID id,
        @Size(min = 14,max = 14, message = "O gtin deve ter 14 caracteres")
        String gtin,
        String description,
        Float avg_price,
        String brand,
        String image
) {}
