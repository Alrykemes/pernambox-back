package com.dev.pernambox.domain.unit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UnitRequestDto (
        UUID id,

        @NotBlank(message = "The name field is required")
        String name,

        UUID addressId
){}
