package com.dev.pernambox.controller;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import com.dev.pernambox.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
@Validated
@Tag(name = "Address", description = "Operações relacionadas a endereços")
public class AddressController {
    private final AddressService addressService;

    @PostMapping()
    @Operation(summary = "Adicionar Endereço", description = "Rota responsável pela inserção de um novo endereço")
    public ResponseEntity<Address> addAddress(@Valid @RequestBody AddressRequestDto addressDto) {
        Address addressResponse = addressService.addAddress(addressDto);
        return  ResponseEntity.ok(addressResponse);
    }

    @PutMapping()
    @Operation(summary = "Editar Endereço", description = "Rota responsável pela edição ou alteração do endereço")
    public ResponseEntity<Address> editAddress(@Valid @RequestBody Address address) {
        Address addressResponse = addressService.editAddress(address);
        return  ResponseEntity.ok(addressResponse);
    }

    @DeleteMapping()
    @Operation(summary = "Deletar Endereço", description = "Rota responsável pela exclusão de um endereço")
    public ResponseEntity<Boolean> deleteAddress(@Valid @RequestBody UUID addressID) {
        boolean response = addressService.deleteAddress(addressID);
        return ResponseEntity.ok(response);
    }
}
