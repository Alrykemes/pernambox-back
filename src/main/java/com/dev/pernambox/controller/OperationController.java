package com.dev.pernambox.controller;

import com.dev.pernambox.domain.operation.dtos.OperationRequestDto;
import com.dev.pernambox.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/operation")
@Tag(name = "Operation", description = "Rotas relacionadas a operações executadas")
public class OperationController {

    private final OperationService operationService;

    @GetMapping("")
    @Operation(summary = "Lista operações", description = "Faz a listagem de todas as operações realizadas")
    public ResponseEntity<List<com.dev.pernambox.domain.operation.Operation>> getAllOperations() {
        return ResponseEntity.ok(operationService.getAllOperations());
    }

    @PostMapping("")
    public ResponseEntity<com.dev.pernambox.domain.operation.Operation> createOperation(@Valid @RequestBody OperationRequestDto body, UriComponentsBuilder uriComponentsBuilder) {
        com.dev.pernambox.domain.operation.Operation newOperation = operationService.createOperation(body);
        URI uri = uriComponentsBuilder.path("/operation/info/{id}").buildAndExpand(newOperation.getId()).toUri();
        return ResponseEntity.created(uri).body(newOperation);
    }

//    se for mudar a forma do get muda no create tbm a URI
//    @GetMapping("/info/{id}")
//    public ResponseEntity<com.dev.pernambox.domain.operation.Operation> getOperationById(@PathVariable Integer id) {
//
//    }
}
