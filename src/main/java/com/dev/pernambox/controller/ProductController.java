package com.dev.pernambox.controller;

import com.dev.pernambox.domain.product.dtos.*;
import com.dev.pernambox.service.ProductService;
import com.dev.pernambox.service.RefProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
@Validated
@Tag(name = "Product", description = "rotas para controle de produtos")
public class ProductController {

    private final ProductService productService;
    private final RefProductService refProductService;

    @GetMapping()
    @Operation(summary = "Lista produtos", description = "Faz a listagem de todos os produtos ordenando pela data de validade")
    public ResponseEntity<List<ProductResponseDto>> findAll() { return ResponseEntity.ok(productService.findAllOrderedByValidity()); }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto", description = "Atualiza um produto existente")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um produto", description = "Remove um produto pelo ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/refProduct")
    @Operation(summary = "Lista produtos base", description = "Faz a listagem de todos os produtos base")
    public ResponseEntity<List<RefProductResponseDto>> findAllRefProducts() { return ResponseEntity.ok(refProductService.findAll()); }

    @GetMapping("/{gtin}")
    @Operation(summary = "Produto base por Gtin", description = "Busca produto base pelo Gtin ( Código de barras )")
    public ResponseEntity<RefProductResponseDto> findByGtin(@Valid @Size(min = 14, max = 14, message = "O gtin deve possuir 14 caracteres") @PathVariable String gtin){ return ResponseEntity.ok(refProductService.findByGtin(gtin)); }

    @PostMapping()
    @Operation(summary = "Adiciona produto", description = "Adiciona um produto novo")
    public ResponseEntity<ProductResponseDto> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.saveProduct(productRequestDto));
    }

    @PostMapping("/refProduct")
    @Operation(summary = "Adiciona produto base", description = "Adiciona um novo produto base")
    public ResponseEntity<RefProductResponseDto> saveProductRef(@Valid @RequestBody RefProductRequestDto refProductRequestDto) {
        return ResponseEntity.ok(refProductService.saveRefProduct(refProductRequestDto));
    }

    @PutMapping("/refProduct")
    @Operation(summary = "Atualiza produto base", description = "Faz a atualização de um produto base")
    public ResponseEntity<RefProductResponseDto> updateRefProduct(@Valid @RequestBody RefProductUpdateDto refProductUpdateDto) {
        return ResponseEntity.ok(refProductService.updateRefProduct(refProductUpdateDto));
    }
}
