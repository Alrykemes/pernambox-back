package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID> {
    List<Operation> findAll();

    <S  extends Operation> S save(S operation);
}
