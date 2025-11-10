package com.dev.pernambox.domain.origin.dtos;

import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.domain.origin.enums.OriginType;

import java.time.LocalDateTime;

public record OriginUpdateDto (
        Integer id,
        String cpf_cnpj_origin,
        DocumentType document,
        LocalDateTime date,
        OriginType origin,
        Integer SEI_process
){}
