package com.dev.pernambox.domain.unit.dtos;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.dtos.UserResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UnitResponseDto(
        UUID id,
        String name,
        UserResponseDto responsible,
        String phone,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        Address address
) {
    public UnitResponseDto(Unit unit) {
        this(unit.getId(), unit.getName(), new UserResponseDto(unit.getResponsible()), unit.getPhone(), unit.getEmail(), unit.getActive(), unit.getCreatedAt(), unit.getAddress());
    }
}