package com.dev.pernambox.service;

import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.domain.product.RefProduct;
import com.dev.pernambox.domain.product.dtos.RefProductRequestDto;
import com.dev.pernambox.domain.product.dtos.RefProductResponseDto;
import com.dev.pernambox.domain.product.dtos.RefProductUpdateDto;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.RefProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RefProductService {

    private final RefProductRepository refProductRepository;

    @Transactional
    public RefProductResponseDto saveRefProduct (RefProductRequestDto refProductRequestDto){
        RefProduct refProduct = new RefProduct(refProductRequestDto);

        if(refProductRepository.findByGtin(refProduct.getGtin()) != null){
            throw new CreateEntityException("Ref product already exists");
        }

        if(refProduct.getAvg_price() <= 0){
            throw new CreateEntityException("Avg price must be greater than 0");
        }

        RefProduct refProductSaved = refProductRepository.save(refProduct);

        return new RefProductResponseDto(refProductSaved);
    }

    @Transactional
    public RefProductResponseDto updateRefProduct (RefProductUpdateDto refProductUpdateDto){
        RefProduct oldRefProduct = refProductRepository.findById(refProductUpdateDto.id()).orElseThrow(() -> {
            throw new NotFoundException("Ref product not found");
        });

        RefProduct newRefProduct = new RefProduct(refProductUpdateDto);

        if(newRefProduct.getGtin() == null){
            newRefProduct.setGtin(oldRefProduct.getGtin());
        }
        if(newRefProduct.getDescription() == null){
            newRefProduct.setDescription(oldRefProduct.getDescription());
        }
        if(newRefProduct.getAvg_price() != null){
            if(newRefProduct.getAvg_price() <= 0){
                throw new CreateEntityException("Avg price must be greater than 0");
            }
        }
        else{
            newRefProduct.setAvg_price(oldRefProduct.getAvg_price());
        }

        if(newRefProduct.getBrand() == null){
            newRefProduct.setBrand(oldRefProduct.getBrand());
        }
        if(newRefProduct.getImage() == null){
            newRefProduct.setImage(oldRefProduct.getImage());
        }

        RefProduct newRefProductSaved = refProductRepository.save(newRefProduct);

        return new RefProductResponseDto(newRefProductSaved);
    }

    public List<RefProductResponseDto> findAll(){
        return refProductRepository.findAll()
                .stream()
                .map(RefProductResponseDto::new)
                .toList();
    }

    public RefProductResponseDto findByGtin(String refProductGtin){
        try {
            RefProduct refProduct = refProductRepository.findByGtin(refProductGtin).orElseThrow(()-> new NotFoundException("Ref product not found"));
            return new RefProductResponseDto(refProduct);
        } catch (NotFoundException e) {
            return new RefProductResponseDto();
        }
    }
}
