package com.dev.pernambox.domain.product;

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

    @Column(name = "gtin", nullable = false, unique = true)
    private String gtin;

    @Column(name = "description", nullable = false)
    private String description;

}
