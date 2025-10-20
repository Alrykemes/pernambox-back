package com.dev.pernambox.domain.operation;

import com.dev.pernambox.domain.operation.enums.Operation_Target;
import com.dev.pernambox.domain.operation.enums.Operation_Type;
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
    private Operation_Type operation_type;

    @Column(name = "operation_date", nullable = false)
    private Date operation_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_target", columnDefinition = "operation_target", nullable = false)
    private Operation_Target operation_target;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "target_id", nullable = false)
    private UUID target_id;

    @Column(name = "unit_id", nullable = false)
    private UUID unit_id;

    @Column(name = "users_id",nullable = false)
    private UUID users_id;
}
