package com.dev.pernambox.controller;

import com.dev.pernambox.domain.loghistory.dtos.LogHistoryResponseDto;
import com.dev.pernambox.domain.loghistory.dtos.PageLogHistoryResponseDto;
import com.dev.pernambox.service.LogHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/log-history")
@Tag(name = "LogHistory", description = "Rotas relacionadas a historico de alterações executadas")
public class LogHistoryController {

    private final LogHistoryService logHistoryService;

    @GetMapping("")
    @Operation(summary = "Lista Paginavel de Historico de Alterações", description = "Faz a listagem de todas as historico de alterações realizadas")
    public ResponseEntity<PageLogHistoryResponseDto> getAllLogHistories(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new PageLogHistoryResponseDto(logHistoryService.getAllLogHistorys((page - 1), size)));
    }

    @Operation(summary = "Informações de um historico de alterações pelo id")
    @GetMapping("/info/{id}")
    public ResponseEntity<LogHistoryResponseDto> getLogHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(new LogHistoryResponseDto(logHistoryService.getLogHistoryById(id)));
    }
}
