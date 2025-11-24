package com.dev.pernambox.domain.product.dtos;

import com.dev.pernambox.domain.product.RefProduct;

import java.util.UUID;

public record RefProductResponseDto(
        UUID id,
        String gtin,
        String description,
        Float avg_price,
        String brand,
        String image
) {
    public RefProductResponseDto(RefProduct refProduct){
        this(refProduct.getId(),refProduct.getGtin(),refProduct.getDescription(),refProduct.getAvg_price(),refProduct.getBrand(),refProduct.getImage());
    }
}