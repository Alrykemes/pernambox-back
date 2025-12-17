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
import com.dev.pernambox.repositories.RefProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RefProductRepository refProductRepository;

    @Mock
    private OriginRepository originRepository;

    @Test
    void shouldSaveProductWhenDataIsValid() {
        RefProduct refProduct = new RefProduct();
        refProduct.setId(UUID.randomUUID());

        Origin origin = new Origin();
        origin.setId((int) (Math.random() * 10));

        ProductRequestDto dto = new ProductRequestDto(
                LocalDateTime.now().plusDays(30),
                500,
                refProduct,
                origin
        );

        when(refProductRepository.findById(refProduct.getId()))
                .thenReturn(Optional.of(refProduct));
        when(originRepository.findById(origin.getId()))
                .thenReturn(Optional.of(origin));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto response = productService.saveProduct(dto);

        assertNotNull(response);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenValidityIsInThePast() {
        RefProduct refProduct = new RefProduct();
        refProduct.setId(UUID.randomUUID());

        Origin origin = new Origin();
        origin.setId((int)(Math.random() * 10));

        ProductRequestDto dto = new ProductRequestDto(
                LocalDateTime.now().minusDays(1),
                100,
                refProduct,
                origin
        );

        when(refProductRepository.findById(refProduct.getId()))
                .thenReturn(Optional.of(refProduct));
        when(originRepository.findById(origin.getId()))
                .thenReturn(Optional.of(origin));

        CreateEntityException ex = assertThrows(
                CreateEntityException.class,
                () -> productService.saveProduct(dto)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("validity"));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        RefProduct refProduct = new RefProduct();
        refProduct.setId(UUID.randomUUID());

        Origin origin = new Origin();
        origin.setId((int)(Math.random() * 10));

        ProductRequestDto dto = new ProductRequestDto(
                LocalDateTime.now().plusDays(10),
                0,
                refProduct,
                origin
        );

        when(refProductRepository.findById(refProduct.getId()))
                .thenReturn(Optional.of(refProduct));
        when(originRepository.findById(origin.getId()))
                .thenReturn(Optional.of(origin));

        CreateEntityException ex = assertThrows(
                CreateEntityException.class,
                () -> productService.saveProduct(dto)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("quantity"));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundWhenRefProductDoesNotExist() {
        RefProduct refProduct = new RefProduct();
        refProduct.setId(UUID.randomUUID());

        Origin origin = new Origin();
        origin.setId((int)(Math.random() * 10));

        ProductRequestDto dto = new ProductRequestDto(
                LocalDateTime.now().plusDays(5),
                10,
                refProduct,
                origin
        );
        when(refProductRepository.findById(refProduct.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> productService.saveProduct(dto)
        );

        verify(productRepository, never()).save(any());
    }
}
