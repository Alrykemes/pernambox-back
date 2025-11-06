package com.dev.pernambox.domain.origin.dtos;

import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.domain.origin.enums.OriginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record OriginRequestDto(
        @NotBlank(message = "O recibo é necessário")
        @Size(max = 255, message = "O recibo só pode ter até 255 caracteres")
        String receipt,
        @NotBlank(message = "O cpf ou cnpj é necessário")
        @Size(min = 11,max = 14, message = "O cpf ou cnpj deve ter entre 11 a 14 caracteres")
        String cpf_cnpj_origin,
        @NotBlank(message = "O tipo do documento é necessário")
        DocumentType document,
        @NotNull(message = "A data é necessária")
        LocalDateTime date,
        @NotBlank(message = "O tipo da origem é necessário")
        OriginType origin,
        Integer SEI_process,
        @Size(max = 255, message = "A ordem só pode ter até 255 caracteres")
        String order,
        @NotBlank(message = "O nome do documento é necessário")
        @Size(max = 255, message = "O nome dos documentos só pode ter até 255 caracteres")
        String documents_name
        ){}