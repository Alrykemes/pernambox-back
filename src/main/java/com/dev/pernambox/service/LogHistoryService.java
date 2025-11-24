package com.dev.pernambox.service;

import com.dev.pernambox.domain.loghistory.LogHistory;
import com.dev.pernambox.domain.loghistory.dtos.LogHistoryWithUnitDto;
import com.dev.pernambox.domain.loghistory.dtos.LogHistoryWithoutUnitDto;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.LogHistoryRepository;
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
public class LogHistoryService {
    private final LogHistoryRepository logHistoryRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;

    public Page<LogHistory> getAllLogHistorys(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return this.logHistoryRepository.getAll(pageable);
    }

    public void createLogHistory(LogHistoryWithoutUnitDto LogHistoryDto) {
        LogHistory newLogHistory = new LogHistory(LogHistoryDto);

        newLogHistory.setUser(userRepository.findById(LogHistoryDto.userId()).orElseThrow(() -> new NotFoundException("User Not Found!")));
        if (!newLogHistory.getLogHistoryType().equals(LogHistoryType.DELETE)) {
            this.verifyTarget(LogHistoryDto.logHistoryTarget(), LogHistoryDto.targetId());
        }
        newLogHistory.setDateTime(LocalDateTime.now());

        this.logHistoryRepository.save(newLogHistory);
    }

    public void createLogHistory(LogHistoryWithUnitDto LogHistoryDto) {
        LogHistory newLogHistory = new LogHistory(LogHistoryDto);

        newLogHistory.setUser(userRepository.findById(LogHistoryDto.userId()).orElseThrow(() -> new NotFoundException("User Not Found!")));
        this.verifyTarget(LogHistoryDto.logHistoryTarget(), LogHistoryDto.targetId());
        newLogHistory.setDateTime(LocalDateTime.now());

        this.logHistoryRepository.save(newLogHistory);
    }

    public LogHistory getLogHistoryById(Long id) {
        return this.logHistoryRepository.findById(id).orElseThrow(() -> new NotFoundException("LogHistory Não encontrada!"));
    }

    private void verifyTarget(LogHistoryTarget logHistoryTarget, UUID uuid) {
        if (logHistoryTarget.equals(LogHistoryTarget.USER)) {
            userRepository.findById(uuid).orElseThrow(() -> new NotFoundException("User Not Found!"));
        }

        if (logHistoryTarget.equals(LogHistoryTarget.UNIT)) {
            unitRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Unit Not Found!"));
        }

        if (logHistoryTarget.equals(LogHistoryTarget.PRODUCT)) {
//                userService.getUserById(LogHistoryDto.targetId());
//                Pegar de product repository
        }

        if (logHistoryTarget.equals(LogHistoryTarget.RESOURCE)) {
//                userService.getUserById(LogHistoryDto.targetId());
//                pegar de resource repository
        }

        if (logHistoryTarget.equals(LogHistoryTarget.RESOURCE_PRODUCT)) {
//                userService.getUserById(LogHistoryDto.targetId());
//                pegar de resource repository
        }

        if (logHistoryTarget.equals(LogHistoryTarget.ORIGIN)) {
//                userService.getUserById(LogHistoryDto.targetId());
//                pegar de origin repository
        }

        if (logHistoryTarget.equals(LogHistoryTarget.DESTINATION)) {
//                userService.getUserById(LogHistoryDto.targetId());
//                pegar de destination repository
        }
    }
}
