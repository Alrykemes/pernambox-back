package com.dev.pernambox.domain.loghistory.dtos;

import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record LogHistoryWithUnitDto(
        @NotBlank(message = "operationType é necessário")
        LogHistoryType logHistoryType,
        @NotBlank(message = "operationTarget é necessário")
        LogHistoryTarget logHistoryTarget,
        @NotBlank(message = "description é necessário")
        String description,
        @NotBlank(message = "targetId é necessário")
        UUID targetId,
        UUID unitId,
        @NotBlank(message = "userId é necessário")
        UUID userId
) {
}
