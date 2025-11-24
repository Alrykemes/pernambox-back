package com.dev.pernambox.domain.product.dtos;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.product.Product;
import com.dev.pernambox.domain.product.RefProduct;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        LocalDateTime validity,
        Integer quantity,
        RefProduct ref_product_id,
        Origin origin_id
) {
    public ProductResponseDto(Product product){
        this(product.getId(), product.getValidity(), product.getQuantity(), product.getRef_product_id(), product.getOrigin_id());
    }
}
