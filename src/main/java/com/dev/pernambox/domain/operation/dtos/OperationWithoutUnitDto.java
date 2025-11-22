package com.dev.pernambox.domain.operation.dtos;

import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.domain.operation.enums.OperationType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record OperationWithoutUnitDto(
        @NotBlank(message = "operationType é necessário")
        OperationType operationType,
        @NotBlank(message = "operationTarget é necessário")
        OperationTarget operationTarget,
        @NotBlank(message = "description é necessário")
        String description,
        @NotBlank(message = "targetId é necessário")
        UUID targetId,
        @NotBlank(message = "userId é necessário")
        UUID userId
) {
}
