package com.dev.pernambox.domain.loghistory.dtos;

import com.dev.pernambox.domain.loghistory.LogHistory;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import com.dev.pernambox.domain.user.dtos.UserResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogHistoryResponseDto(
        Long id,
        String description,
        LogHistoryType logHistoryType,
        LogHistoryTarget logHistoryTarget,
        LocalDateTime dateTime,
        UUID targetId,
        UUID unitId,
        UserResponseDto responsibleUser
) {
    public LogHistoryResponseDto(LogHistory logHistory) {
        this(
                logHistory.getId(),
                logHistory.getDescription(),
                logHistory.getLogHistoryType(),
                logHistory.getLogHistoryTarget(),
                logHistory.getDateTime(),
                logHistory.getTargetId(),
                logHistory.getUnitId(),
                new UserResponseDto(logHistory.getUser())
        );
    }
}