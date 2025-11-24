package com.dev.pernambox.domain.product;

import com.dev.pernambox.domain.product.dtos.RefProductRequestDto;
import com.dev.pernambox.domain.product.dtos.RefProductUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "RefProduct")
@Table(name = "ref_product", schema = "public")
public class RefProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(name = "gtin", nullable = false, unique = true)
    private String gtin;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "avg_price", nullable = false)
    private Float avg_price;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "image", nullable = false)
    private String image;

    public RefProduct(RefProductRequestDto dto) {
        this.gtin = dto.gtin();
        this.description = dto.description();
        this.avg_price = dto.avg_price();
        this.brand = dto.brand();
        this.image = dto.image();
    }

    public RefProduct(RefProductUpdateDto dto) {
        this.id = dto.id();
        this.gtin = dto.gtin();
        this.description = dto.description();
        this.avg_price = dto.avg_price();
        this.brand = dto.brand();
        this.image = dto.image();
    }

    public RefProduct() {}
}
