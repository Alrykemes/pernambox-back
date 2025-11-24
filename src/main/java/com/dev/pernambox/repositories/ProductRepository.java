package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p ORDER BY p.validity DESC")
    List<Product> findAllOrderedByValidity();
}
