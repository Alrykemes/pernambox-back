package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.PreStatsUnitDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
    @Query("""
            SELECT
            COUNT(u),
            SUM(CASE WHEN u.active = true THEN 1 ELSE 0 END),
            SUM(CASE WHEN u.active = false THEN 1 ELSE 0 END)
            FROM Unit u
            """)
    PreStatsUnitDto getCountUnits();

    @Query("SELECT u FROM Unit u ORDER BY u.createdAt DESC LIMIT 1")
    Optional<Unit> findLastInsert();
}
