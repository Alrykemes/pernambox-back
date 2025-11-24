package com.dev.pernambox.domain.loghistory;

import com.dev.pernambox.domain.loghistory.converters.LogHistoryTargetConverter;
import com.dev.pernambox.domain.loghistory.converters.LogHistoryTypeConverter;
import com.dev.pernambox.domain.loghistory.dtos.LogHistoryWithUnitDto;
import com.dev.pernambox.domain.loghistory.dtos.LogHistoryWithoutUnitDto;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import com.dev.pernambox.domain.loghistory.enums.PostgreLogHistoryTargetEnum;
import com.dev.pernambox.domain.loghistory.enums.PostgreLogHistoryTypeEnum;
import com.dev.pernambox.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "LogHistory")
@Table(name = "log_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Convert(converter = LogHistoryTypeConverter.class)
    @Type(PostgreLogHistoryTypeEnum.class)
    @Column(name = "log_history_type", columnDefinition = "log_history_type", nullable = false)
    private LogHistoryType logHistoryType;

    @Column(name = "log_history_date", nullable = false)
    private LocalDateTime dateTime;

    @Convert(converter = LogHistoryTargetConverter.class)
    @Type(PostgreLogHistoryTargetEnum.class)
    @Column(name = "log_history_target", columnDefinition = "log_history_target", nullable = false)
    private LogHistoryTarget logHistoryTarget;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Column(name = "unit_id")
    private UUID unitId;

    @JoinColumn(name = "users_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public LogHistory(LogHistoryWithoutUnitDto dto) {
        this.logHistoryType = dto.logHistoryType();
        this.logHistoryTarget = dto.logHistoryTarget();
        this.description = dto.description();
        this.targetId = dto.targetId();
    }

    public LogHistory(LogHistoryWithUnitDto dto) {
        this.logHistoryType = dto.logHistoryType();
        this.logHistoryTarget = dto.logHistoryTarget();
        this.description = dto.description();
        this.unitId = dto.unitId();
        this.targetId = dto.targetId();
    }
}
