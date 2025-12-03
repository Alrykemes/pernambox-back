package com.dev.pernambox.domain.product.dtos;

import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.domain.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        LocalDateTime validity,
        Integer quantity,
        RefProductResponseDto refProduct,
        OriginResponseDto origin
) {
    public ProductResponseDto(Product product){
        this(
                product.getId(),
                product.getValidity(),
                product.getQuantity(),
                new RefProductResponseDto(product.getRef_product_id()),
                new OriginResponseDto(product.getOrigin_id())
        );
    }
}
