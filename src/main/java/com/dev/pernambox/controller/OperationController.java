package com.dev.pernambox.controller;

import com.dev.pernambox.service.OperationService;
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
@RequestMapping("/operation")
@Tag(name = "Operation", description = "Rotas relacionadas a operações executadas")
public class OperationController {

    private final OperationService operationService;

    @GetMapping("")
    @Operation(summary = "Lista operações", description = "Faz a listagem de todas as operações realizadas")
    public ResponseEntity<List<com.dev.pernambox.domain.operation.Operation>> getAllOperations()
    {
        try{
            operationService.getAllOperations();
            return ResponseEntity.ok(operationService.getAllOperations());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Boolean> createOperation(@Valid @RequestBody com.dev.pernambox.domain.operation.Operation operation)
    {
        try{
            var response = operationService.createOperation(operation);

            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
