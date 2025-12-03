package com.dev.pernambox.service;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.product.Product;
import com.dev.pernambox.domain.product.RefProduct;
import com.dev.pernambox.domain.product.dtos.ProductRequestDto;
import com.dev.pernambox.domain.product.dtos.ProductResponseDto;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.OriginRepository;
import com.dev.pernambox.repositories.ProductRepository;
import java.util.UUID;
import com.dev.pernambox.repositories.RefProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RefProductRepository refProductRepository;
    private final OriginRepository originRepository;

    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto productDto){

        Product product = new Product(productDto);

        RefProduct refProduct = refProductRepository.findById(
                productDto.ref_product_id().getId()
        ).orElseThrow(() -> new NotFoundException("Product reference not found"));

        Origin origin = originRepository.findById(
                productDto.origin_id().getId()
        ).orElseThrow(() -> new NotFoundException("Origin not found"));

        LocalDateTime validity = productDto.validity();

        if (validity.isBefore(LocalDateTime.now()) || validity.isEqual(LocalDateTime.now())) {
            throw new CreateEntityException("Product validity cannot be in the past or equal to today");
        }

        if(productDto.quantity() <= 0){
            throw new CreateEntityException("Product quantity must be greater than 0");
        }

        product.setRef_product_id(refProduct);
        product.setOrigin_id(origin);

        Product response = productRepository.save(product);

        return new ProductResponseDto(response);
    }

    public List<ProductResponseDto> findAllOrderedByValidity(){
        return productRepository.findAllOrderedByValidity()
                .stream()
                .map(ProductResponseDto::new)
                .toList();
    }


    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        RefProduct refProduct = refProductRepository.findById(dto.ref_product_id().getId())
                .orElseThrow(() -> new NotFoundException("Product reference not found"));

        Origin origin = originRepository.findById(dto.origin_id().getId())
                .orElseThrow(() -> new NotFoundException("Origin not found"));

        LocalDateTime validity = dto.validity();

        if (validity.isBefore(LocalDateTime.now()) || validity.isEqual(LocalDateTime.now())) {
            throw new CreateEntityException("Product validity cannot be in the past or equal to today");
        }

        if(dto.quantity() <= 0){
            throw new CreateEntityException("Product quantity must be greater than 0");
        }

        product.setQuantity(dto.quantity());
        product.setValidity(dto.validity());
        product.setRef_product_id(refProduct);
        product.setOrigin_id(origin);

        Product updated = productRepository.saveAndFlush(product);
        return new ProductResponseDto(updated);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productRepository.delete(product);
    }
}
