package com.dev.pernambox.domain.product;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.product.dtos.ProductRequestDto;
import com.dev.pernambox.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
@Table(name = "product", schema = "public")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(name = "validity", nullable = false, updatable = false)
    private LocalDateTime validity;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_product_id", nullable = false)
    private RefProduct ref_product_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", nullable = false)
    private Origin origin_id;

    public Product (ProductRequestDto dto){
        this.validity = dto.validity();
        this.quantity = dto.quantity();
        this.ref_product_id = dto.ref_product_id();
        this.origin_id = dto.origin_id();
    }
}
