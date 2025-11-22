package com.dev.pernambox.service;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.domain.operation.dtos.OperationWithUnitDto;
import com.dev.pernambox.domain.operation.dtos.OperationWithoutUnitDto;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.exceptions.CreateEntityException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.OperationRepository;
import com.dev.pernambox.repositories.UnitRepository;
import com.dev.pernambox.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;

    public Page<Operation> getAllOperations(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return this.operationRepository.findAll(pageable);
    }

    public void createOperation(OperationWithoutUnitDto operationDto) {
        Operation newOperation = new Operation(operationDto);

        newOperation.setUser(userRepository.findById(operationDto.userId()).orElseThrow(() -> new NotFoundException("User Not Found!")));
        this.verifyTarget(operationDto.operationTarget(), operationDto.targetId());
        newOperation.setOperationDate(LocalDateTime.now());

        this.operationRepository.save(newOperation);
    }

    public void createOperation(OperationWithUnitDto operationDto) {
        Operation newOperation = new Operation(operationDto);

        newOperation.setUser(userRepository.findById(operationDto.userId()).orElseThrow(() -> new NotFoundException("User Not Found!")));
        this.verifyTarget(operationDto.operationTarget(), operationDto.targetId());
        newOperation.setOperationDate(LocalDateTime.now());

        this.operationRepository.save(newOperation);
    }

    public Operation getOperationById(UUID id) {
        return this.operationRepository.findById(id).orElseThrow(() -> new NotFoundException("Operation Não encontrada!"));
    }

    private void verifyTarget(OperationTarget operationTarget, UUID uuid) {
        if (operationTarget.equals(OperationTarget.USER)) {
            userRepository.findById(uuid).orElseThrow(() -> new NotFoundException("User Not Found!"));
        }

        if (operationTarget.equals(OperationTarget.UNIT)) {
            unitRepository.findById(uuid).orElseThrow(() -> new NotFoundException("User Not Found!"));
        }

        if (operationTarget.equals(OperationTarget.PRODUCT)) {
//                userService.getUserById(operationDto.targetId());
//                Pegar de product repository
        }

        if (operationTarget.equals(OperationTarget.RESOURCE)) {
//                userService.getUserById(operationDto.targetId());
//                pegar de resource repository
        }

        if (operationTarget.equals(OperationTarget.RESOURCE_PRODUCT)) {
//                userService.getUserById(operationDto.targetId());
//                pegar de resource repository
        }

        if (operationTarget.equals(OperationTarget.ORIGIN)) {
//                userService.getUserById(operationDto.targetId());
//                pegar de origin repository
        }

        if (operationTarget.equals(OperationTarget.DESTINATION)) {
//                userService.getUserById(operationDto.targetId());
//                pegar de destination repository
        }
    }
}
