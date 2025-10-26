package com.dev.pernambox.service;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.repositories.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UnitService {
    private final UnitRepository unitRepository;

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }
}
