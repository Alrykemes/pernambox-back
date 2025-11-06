package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.dtos.StatsUsersResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
    @Query("""
            SELECT COUNT(u) FROM User u
            """)
    Long getCountUnits();

    @Query("SELECT u FROM Unit u ORDER BY u.createdAt DESC")
    Optional<Unit> findLastInsert();
}
