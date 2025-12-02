package com.dev.pernambox.domain.resource.dtos;

import com.dev.pernambox.domain.resource.enums.CategoriesResources;
import com.dev.pernambox.domain.resource.enums.StatusType;
import com.dev.pernambox.domain.unit.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceRequestDto(
        @NotBlank(message = "A descrição é necessária")
        String description,
        @NotNull(message = "O status do recurso é necessário")
        StatusType status,
        @NotBlank(message = "A quantidade é necessária")
        Integer quantity,
        @NotNull(message = "A categoria do recurso é necessária")
        CategoriesResources category,
        @NotNull(message = "A unidade do recurso é necessária")
        Unit unit_id,
        @NotBlank(message = "O qrcode é necessário")
        String qrcode
) {}
