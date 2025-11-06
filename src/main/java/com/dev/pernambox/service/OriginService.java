package com.dev.pernambox.service;

import com.dev.pernambox.domain.origin.Origin;
import com.dev.pernambox.domain.origin.dtos.OriginRequestDto;
import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.repositories.OriginRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Service
public class OriginService {
    private final OriginRepository originRepository;

    @Transactional
    public Origin saveOrigin(OriginRequestDto originDto) {
        try{
            Origin origin = new Origin(originDto);

            if (origin.getDocument() == DocumentType.CPF) {
                CPFValidator cpfValidator = new CPFValidator();
                if(!cpfValidator.isValid(origin.getCpf_cnpj_origin(), null)){
                    throw new Exception("CPF inválido");
                }
            }

            if (origin.getDocument() == DocumentType.CNPJ) {
                CNPJValidator cnpjValidator = new CNPJValidator();
                if (!cnpjValidator.isValid(origin.getCpf_cnpj_origin(), null)) {
                    throw new Exception("CNPJ inválido");
                }
            }

            if(origin.getDate() > LocalDateTime.now()){

            }

            return originRepository.save(origin);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
