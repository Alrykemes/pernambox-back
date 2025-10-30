package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
    List<Unit> findAll();

    Unit save(Unit unit);

    void delete(Unit unit);

    Unit findByName(String name);

    Unit findUnitById(UUID id);

    Unit findUnitByAddress(Address address);
}
