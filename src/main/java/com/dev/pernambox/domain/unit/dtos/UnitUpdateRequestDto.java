package com.dev.pernambox.domain.unit.dtos;

import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UnitUpdateRequestDto(
        @Size(min = 6, max = 150, message = "O nome precisa ter no mínimo 6 caracteres e no máximo 150")
        String name,
        UUID responsible_id,
        @Size(min = 11, max = 11, message = "O telefone precisa ter 11 carateres")
        String phone,
        @Email(message = "Email precisa ser um email válido")
        String email,
        Boolean active,
        AddressRequestDto address
) {}