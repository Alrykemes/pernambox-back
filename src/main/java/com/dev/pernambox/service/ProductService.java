package com.dev.pernambox.service;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.domain.product.Product;
import com.dev.pernambox.domain.product.RefProduct;
import com.dev.pernambox.domain.product.dtos.ProductRequestDto;
import com.dev.pernambox.domain.product.dtos.ProductResponseDto;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.OriginRepository;
import com.dev.pernambox.repositories.ProductRepository;
import com.dev.pernambox.repositories.RefProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RefProductRepository refProductRepository;
    private final OriginRepository originRepository;

    @Transactional
    public ProductResponseDto saveProduct (ProductRequestDto productDto){
        Product product = new Product(productDto);
        RefProduct refProduct = refProductRepository.findById(product.getRef_product_id().getId()).orElseThrow(() -> {
            throw new NotFoundException("Product reference not found");
        });

        Origin originProduct = originRepository.findById(product.getOrigin_id().getId()).orElseThrow(() -> {
            throw new NotFoundException("Origin not found");
        });

        if(product.getValidity().isAfter(LocalDateTime.now())){
            throw new CreateEntityException("Product validity cannot be after now");
        }

        if(product.getQuantity() <= 0){
            throw new CreateEntityException("Product quantity cannot be less than 0");
        }

        Product response = productRepository.save(product);

        ProductResponseDto responseDto = new ProductResponseDto(response);

        return responseDto;
    }

    public List<ProductResponseDto> findAllOrderedByValidity(){
        return productRepository.findAllOrderedByValidity()
                .stream()
                .map(ProductResponseDto::new)
                .toList();
    }
}
