package com.dev.pernambox.domain.unit.dtos;


import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UnitCreateRequestDto(
        @NotBlank @Size(max = 150) String name,
        @NotNull UUID responsible_id,
        @NotBlank @Size(max = 11)  String phone,
        @NotBlank @Size(max = 255) String email,
        @NotNull AddressRequestDto address
) {
}
