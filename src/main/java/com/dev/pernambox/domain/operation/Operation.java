package com.dev.pernambox.domain.operation;

import com.dev.pernambox.domain.operation.dtos.OperationWithUnitDto;
import com.dev.pernambox.domain.operation.dtos.OperationWithoutUnitDto;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.domain.operation.enums.OperationType;
import com.dev.pernambox.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Operation")
@Table(name = "operation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", columnDefinition = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "operation_date", nullable = false)
    private LocalDateTime operationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_target", columnDefinition = "operation_target", nullable = false)
    private OperationTarget operationTarget;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Column(name = "unit_id")
    private UUID unitId;

    @JoinColumn(name = "users_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public Operation(OperationWithoutUnitDto dto) {
        this.operationType = dto.operationType();
        this.operationTarget = dto.operationTarget();
        this.description = dto.description();
        this.targetId = dto.targetId();
    }

    public Operation(OperationWithUnitDto dto) {
        this.operationType = dto.operationType();
        this.operationTarget = dto.operationTarget();
        this.description = dto.description();
        this.unitId = dto.unitId();
        this.targetId = dto.targetId();
    }
}
