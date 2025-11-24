package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.product.RefProduct;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RefProductRepository extends JpaRepository<RefProduct, UUID> {
    @Query("SELECT rp FROM RefProduct rp WHERE rp.gtin = :refProductGtin")
    Optional<RefProduct> findByGtin(@Param("refProductGtin") String refProductGtin);
}
