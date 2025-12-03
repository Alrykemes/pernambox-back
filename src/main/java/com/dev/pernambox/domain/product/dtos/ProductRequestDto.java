package com.dev.pernambox.domain.product.dtos;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.domain.origin.enums.OriginType;
import com.dev.pernambox.domain.product.RefProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record ProductRequestDto(
        @NotNull
        LocalDateTime validity,
        @NotNull
        @Min(1)
        Integer quantity,
        @NotNull(message = "A referência do produto é necessária")
        RefProduct ref_product_id,
        @NotNull(message = "A origem do produto é necessária")
        Origin origin_id
) {
}

