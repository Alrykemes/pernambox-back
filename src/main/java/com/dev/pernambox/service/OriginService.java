package com.dev.pernambox.service;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.origin.dtos.OriginRequestDto;
import com.dev.pernambox.domain.origin.dtos.OriginResponseDto;
import com.dev.pernambox.domain.origin.dtos.OriginUpdateDto;
import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.repositories.OriginRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OriginService {
    private final OriginRepository originRepository;

    @Transactional
    public Origin saveOrigin(OriginRequestDto originDto) {
        try {
            Origin origin = new Origin(originDto);

            if (origin.getDocument() == DocumentType.CPF) {
                CPFValidator cpfValidator = new CPFValidator();
                if (!cpfValidator.isValid(origin.getCpf_cnpj_origin(), null)) {
                    throw new Exception("CPF inválido");
                }
            }

            if (origin.getDocument() == DocumentType.CNPJ) {
                CNPJValidator cnpjValidator = new CNPJValidator();
                if (!cnpjValidator.isValid(origin.getCpf_cnpj_origin(), null)) {
                    throw new Exception("CNPJ inválido");
                }
            }

            if (origin.getDate().isAfter(LocalDateTime.now())) {
                throw new Exception("Data inválida");
            }

            return originRepository.save(origin);
        } catch (Exception e) {
            throw new CreateEntityException(e.getMessage());
        }
    }

    @Transactional
    public Origin updateOrigin(OriginUpdateDto originDto) {
        try {
            Origin oldOrigin = originRepository.findById(originDto.id()).orElseThrow(() -> { throw new NotFoundException("Origin not found"); });

            Origin newOrigin = new Origin(originDto);

            if(newOrigin.getCpf_cnpj_origin() != null){
                if (newOrigin.getDocument() == DocumentType.CPF) {
                    CPFValidator cpfValidator = new CPFValidator();
                    if (!cpfValidator.isValid(newOrigin.getCpf_cnpj_origin(), null)) {
                        throw new Exception("CPF inválido");
                    }
                }

                if (newOrigin.getDocument() == DocumentType.CNPJ) {
                    CNPJValidator cnpjValidator = new CNPJValidator();
                    if (!cnpjValidator.isValid(newOrigin.getCpf_cnpj_origin(), null)) {
                        throw new Exception("CNPJ inválido");
                    }
                }
            }
            else {
                newOrigin.setCpf_cnpj_origin(oldOrigin.getCpf_cnpj_origin());
            }

            if(newOrigin.getDate() != null){
                if (newOrigin.getDate().isAfter(LocalDateTime.now())) {
                    throw new Exception("Data inválida");
                }
            }
            else {
                newOrigin.setDate(oldOrigin.getDate());
            }

            if(newOrigin.getDocument() == null){ newOrigin.setDocument(oldOrigin.getDocument());}
            if(newOrigin.getOrigin() == null){ newOrigin.setOrigin(oldOrigin.getOrigin());}
            if(newOrigin.getSEI_process() == null){ newOrigin.setSEI_process(oldOrigin.getSEI_process());}


            return originRepository.save(newOrigin);

        } catch (Exception e) {
            throw new UpdateEntityException(e.getMessage());
        }
    }

    public List<OriginResponseDto> findAllOrigins() {
        return originRepository.findAll()
                .stream()
                .map(OriginResponseDto::new)
                .toList();
    }
}
