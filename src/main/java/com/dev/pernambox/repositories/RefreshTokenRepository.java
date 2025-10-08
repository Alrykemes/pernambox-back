package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshToken> findByToken(UUID token);

    @Query("SELECT r FROM RefreshToken r WHERE r.user.id = :userId")
    Optional<RefreshToken> findByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken r SET r.token = :newToken, r.expirationDate = :expirationDate WHERE r.token = :token")
    int updateRefreshByToken(
            @Param("token") UUID token,
            @Param("newToken") UUID newToken,
            @Param("expirationDate") LocalDateTime expirationDate
    );
}
