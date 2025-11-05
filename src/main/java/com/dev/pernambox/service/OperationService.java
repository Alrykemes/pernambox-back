package com.dev.pernambox.service;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.domain.operation.dtos.OperationRequestDto;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final UserService userService;
//    private final UnitService unitService;

    public List<Operation> getAllOperations() {
        return this.operationRepository.findAll();
    }

    public Operation createOperation(OperationRequestDto operationDto) {
        Operation newOperation = new Operation(operationDto);

        newOperation.setUser(userService.getUserById(operationDto.userId()));
//        newOperation.setUser(unitService.getUnitById(operationDto.unitId()));
        this.verifyTarget(operationDto);
        newOperation.setOperationDate(new Date());

        return this.operationRepository.save(newOperation);
    }


    private void verifyTarget(OperationRequestDto operationDto) {
        if(operationDto.operationTarget().equals(OperationTarget.USER)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.UNIT)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.PRODUCT)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.RESOURCE)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.RESOURCE_PRODUCT)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.ORIGIN)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
        if(operationDto.operationTarget().equals(OperationTarget.DESTINATION)) {
            try {
                userService.getUserById(operationDto.targetId());
            } catch (NotFoundException ex) {
                throw new CreateEntityException("Erro ao registrar alteração, id alvo inválido!");
            }
        }
    }
}
