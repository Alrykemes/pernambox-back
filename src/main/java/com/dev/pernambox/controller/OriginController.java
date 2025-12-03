package com.dev.pernambox.controller;

import com.dev.pernambox.domain.origin.dtos.OriginRequestDto;
import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.domain.origin.dtos.OriginUpdateDto;
import com.dev.pernambox.service.OriginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/origin")
@Tag(name = "Origin", description = "Rotas relacionadas a origens de produtos e kits (recursos)")
public class OriginController {

    private final OriginService originService;

    @GetMapping("")
    @Operation(summary = "Lista origens", description = "Faz a listagem de todas as origens disponiveis")
    public ResponseEntity<List<OriginResponseDto>> getAllOperations() {
        return ResponseEntity.ok(originService.findAllOrigins());
    }

    @PostMapping("")
    @Operation(summary = "Adicionar origem", description = "Adiciona uma nova origem")
    public ResponseEntity<OriginResponseDto> createOperation(@Valid @RequestBody OriginRequestDto originRequestDto) {
        return ResponseEntity.ok(originService.saveOrigin(originRequestDto));
    }

    @PutMapping("")
    @Operation(summary = "Atualizar origem", description = "Faz a atualização de uma origem")
    public ResponseEntity<OriginResponseDto> updateOperation(@Valid @RequestBody OriginUpdateDto originUpdateDto) {
        return ResponseEntity.ok(originService.updateOrigin(originUpdateDto));
    }
}
