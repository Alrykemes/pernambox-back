package com.dev.pernambox.service;

import com.dev.pernambox.domain.operation.Operation;
import com.dev.pernambox.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;

    public List<Operation> getAllOperations()
    {
        return this.operationRepository.findAll();
    }

    public boolean createOperation(Operation operation)
    {
        operation.setOperation_date(new Date());
        return this.operationRepository.save(operation) != null;
    }
}
