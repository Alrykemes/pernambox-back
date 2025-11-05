package com.dev.pernambox.domain.unit.dtos;

import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import java.util.UUID;

public record UnitUpdateRequestDto(
        String name,
        UUID responsible_id,
        String phone,
        String email,
        AddressRequestDto address
) {}