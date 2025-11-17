package com.dev.pernambox.controller;

import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.service.OriginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
