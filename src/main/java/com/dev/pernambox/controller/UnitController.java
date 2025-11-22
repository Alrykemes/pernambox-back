package com.dev.pernambox.controller;

import com.dev.pernambox.domain.operation.dtos.OperationWithoutUnitDto;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.domain.operation.enums.OperationType;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.UnitCreateRequestDto;
import com.dev.pernambox.domain.unit.dtos.UnitStatsResponseDto;
import com.dev.pernambox.domain.unit.dtos.UnitUpdateRequestDto;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.exceptions.AuthorizationException;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.DeleteEntityException;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.service.OperationService;
import com.dev.pernambox.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unit")
@Validated
@Tag(name = "Unit", description = "rotas para controle de unidade")
public class UnitController {
    private final UnitService unitService;
    private final OperationService operationService;

    @GetMapping()
    @Operation(summary = "Lista todas unidades", description = "Faz a listagem de todas as unidades cadastradas")
    public ResponseEntity<List<Unit>> findAll() {
        return ResponseEntity.ok(unitService.findAll());
    }

    @PostMapping("/create")
    @Operation(summary = "Adiciona unidade", description = "Faz a inserção de uma unidade")
    public ResponseEntity<Unit> saveUnit(@Valid @RequestBody UnitCreateRequestDto unitDto, UriComponentsBuilder uriComponentsBuilder, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if(user.getRole().equals(Role.USER) || user.getRole().equals(Role.ADMIN)) {
            throw new CreateEntityException("Você não tem permissão para criar unidades");
        }
        Unit newUnit = unitService.saveUnit(unitDto, user);

        operationService.createOperation(new OperationWithoutUnitDto(
                OperationType.CREATE,
                OperationTarget.UNIT,
                this.getDescriptionOperation(OperationType.CREATE, user, newUnit),
                newUnit.getId(),
                user.getId()
        ));

        URI uri = uriComponentsBuilder.path("/user/info/{id}").buildAndExpand(newUnit.getId()).toUri();
        return ResponseEntity.created(uri).body(newUnit);
    }

    @PatchMapping("/update/{unitId}")
    @Operation(summary = "Atualiza unidade", description = "Faz a alteração da unidade desejada")
    public ResponseEntity<Unit> updateUnit(
            @PathVariable UUID unitId,
            @Valid @RequestBody UnitUpdateRequestDto unitDto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        if(user.getRole().equals(Role.USER) || user.getRole().equals(Role.ADMIN)) {
            throw new UpdateEntityException("Você não tem permissão para Atualizar unidades");
        }

        Unit updatedUnit = unitService.updateUnit(unitId, unitDto);

        operationService.createOperation(new OperationWithoutUnitDto(
                OperationType.UPDATE,
                OperationTarget.UNIT,
                this.getDescriptionOperation(OperationType.UPDATE, user, updatedUnit),
                updatedUnit.getId(),
                user.getId()
        ));

        return ResponseEntity.ok(updatedUnit);
    }

    @DeleteMapping("/delete/{unitId}")
    @Operation(summary = "Deleta uma unidade", description = "Faz a deleção de uma unidade")
    public void deleteUnit(@PathVariable UUID unitId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if(user.getRole().equals(Role.USER) || user.getRole().equals(Role.ADMIN)) {
            throw new DeleteEntityException("Você não tem permissão para deletar unidades");
        }
        Unit deletedUnit = unitService.deleteUnit(unitId);

        operationService.createOperation(new OperationWithoutUnitDto(
                OperationType.DELETE,
                OperationTarget.UNIT,
                this.getDescriptionOperation(OperationType.DELETE, user, deletedUnit),
                deletedUnit.getId(),
                user.getId()
        ));
    }

    @GetMapping("info/stats")
    @Operation(summary = "Retorna status das unidades", description = "Retorna total e ultima unidade adicionada")
    public ResponseEntity<UnitStatsResponseDto> getStatsUnits() {
        return ResponseEntity.ok(unitService.getUnitStats());
    }

    private String getDescriptionOperation(OperationType type, User userResponsible, Unit unitAffected) {
        return type.equals(OperationType.CREATE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Criou a unidade " + unitAffected.getName() + " de id " + unitAffected.getId().toString()
                : type.equals(OperationType.UPDATE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Atualizou atualizou a unidade " + unitAffected.getName() + " de id " + unitAffected.getId().toString()
                : type.equals(OperationType.DELETE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Deletou a unidade " + unitAffected.getName() + " de id " + unitAffected.getId().toString()
                : "Tipo de Operação Inválido";
    }
}
