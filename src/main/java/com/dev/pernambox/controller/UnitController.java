package com.dev.pernambox.controller;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.UnitRequestDto;
import com.dev.pernambox.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unit")
@Validated
@Tag(name="Unit", description = "rotas para controle de unidade")
public class UnitController {
    private final UnitService unitService;

    @GetMapping()
    @Operation(summary = "Lista todas unidades", description = "Faz a listagem de todas as unidades cadastradas")
    public List<Unit> findAll() {
        return unitService.findAll();
    }

    @PostMapping()
    @Operation(summary = "Adiciona unidade", description = "Faz a inserção de uma unidade")
    public Unit saveUnit(@Valid @RequestBody UnitRequestDto unitDto){
        return unitService.saveUnit(unitDto);
    }
}
