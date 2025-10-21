package com.dev.pernambox.service;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.domain.operation.dtos.OperationRequestDto;
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

        newOperation.setUser(userService.getUserById(operationDto.userId().toString()));
//        Get Unit quando tiver o service de unit
//        newOperation.setUser(unitService.getUnitById(operationDto.unitId()));
        newOperation.setOperationDate(new Date());

        return this.operationRepository.save(newOperation);
    }
}
