package com.dev.pernambox.domain.operation.dtos;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.domain.operation.enums.Operation_Target;
import com.dev.pernambox.domain.operation.enums.Operation_Type;

import java.util.Date;
import java.util.UUID;

public record OperationRequestDto(
        Operation_Type operation_type,
        Date operation_date,
        Operation_Target operation_target,
        String description,
        UUID target_id,
        UUID unit_id,
        UUID users_id
) {
    public OperationRequestDto(Operation operation) {
        this(operation.getOperation_type(), operation.getOperation_date(), operation.getOperation_target(), operation.getDescription(), operation.getTarget_id(), operation.getUnit_id(), operation.getUsers_id());
    }
}
