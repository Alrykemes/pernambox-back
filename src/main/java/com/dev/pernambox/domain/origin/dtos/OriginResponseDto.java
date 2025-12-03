package com.dev.pernambox.domain.origin.dtos;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.domain.origin.enums.OriginType;

import java.time.LocalDateTime;

public record OriginResponseDto(
        Integer id,
        String cpf_cnpj_origin,
        DocumentType document,
        LocalDateTime date,
        OriginType origin,
        Integer SEI_process
) {
    public OriginResponseDto(Origin origin) {
        this(origin.getId(), origin.getCpf_cnpj_origin(), origin.getDocument(), origin.getDate(), origin.getOrigin(), origin.getSEI_process());
    }
}
