package com.dev.pernambox.domain.loghistory.dtos;

import com.dev.pernambox.domain.loghistory.LogHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageLogHistoryResponseDto(int previousPage, int currentPage, int nextPage, int totalPages, int size, List<LogHistoryResponseDto> content) {
    public PageLogHistoryResponseDto(Page<LogHistory> pageOperations) {
        this(
                pageOperations.isFirst() ? 1 : pageOperations.getNumber(),
                pageOperations.getNumber() + 1,
                pageOperations.isLast() ? pageOperations.getNumber() + 1 : pageOperations.getNumber() + 2,
                pageOperations.getTotalPages(),
                pageOperations.getSize(),
                pageOperations.getContent().stream().map(LogHistoryResponseDto::new).toList()
        );
    }
}
