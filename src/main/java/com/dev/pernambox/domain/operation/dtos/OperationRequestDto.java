package com.dev.pernambox.domain.operation.dtos;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.domain.operation.enums.OperationType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

public record OperationRequestDto(
        @NotBlank(message = "operationType é necessário")
        OperationType operationType,
        @NotBlank(message = "operationDate é necessário")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date operationDate,
        @NotBlank(message = "operationTarget é necessário")
        OperationTarget operationTarget,
        @NotBlank(message = "description é necessário")
        String description,
        @NotBlank(message = "targetId é necessário")
        UUID targetId,
        @NotBlank(message = "unitId é necessário")
        UUID unitId,
        @NotBlank(message = "userId é necessário")
        UUID userId
) {
}
