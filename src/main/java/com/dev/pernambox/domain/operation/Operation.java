package com.dev.pernambox.domain.operation;

import com.dev.pernambox.domain.operation.dtos.OperationRequestDto;
import com.dev.pernambox.domain.operation.enums.OperationTarget;
import com.dev.pernambox.domain.operation.enums.OperationType;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
    private Date operationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_target", columnDefinition = "operation_target", nullable = false)
    private OperationTarget operationTarget;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @JoinColumn(name = "unit_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Unit unit;

    @JoinColumn(name = "users_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public Operation(OperationRequestDto dto) {
        this.operationType = dto.operationType();
        this.operationDate = dto.operationDate();
        this.operationTarget = dto.operationTarget();
        this.description = dto.description();
        this.targetId = dto.targetId();
    }
}
