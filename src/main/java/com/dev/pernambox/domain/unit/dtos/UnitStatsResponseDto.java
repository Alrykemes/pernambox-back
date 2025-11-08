package com.dev.pernambox.domain.unit.dtos;

public record UnitStatsResponseDto(Long totalUnits, Long unitsActives, Long unitsDeactivates, UnitResponseDto lastUnitCreated) {
}
