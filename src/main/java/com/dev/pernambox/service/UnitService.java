package com.dev.pernambox.service;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.*;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.UnitRepository;
import com.dev.pernambox.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final LogHistoryService logHistoryService;

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Unit saveUnit(UnitCreateRequestDto unitDto, User userResponsible) {
        AddressRequestDto newAddress = unitDto.address();
        Address address = new Address(newAddress);

        User responsible = userRepository.findById(unitDto.responsible_id())
                .orElseThrow(() -> new NotFoundException("Usuário responsável não encontrado"));

        Unit unit = new Unit();

        unit.setName(unitDto.name());
        unit.setResponsible(responsible);
        unit.setPhone(unitDto.phone());
        unit.setEmail(unitDto.email());
        unit.setActive(true);
        unit.setAddress(address);

        return unitRepository.save(unit);
    }

    public Unit findUnitById(UUID id) {
        return unitRepository.findById(id).orElseThrow(() -> new NotFoundException("Unidade não encontrada"));
    }

    @Transactional
    public Unit updateUnit(UUID id, UnitUpdateRequestDto unitDto) {
        Unit unit = unitRepository.findById(id).orElseThrow(() -> new NotFoundException("Unidade não encontrada para ser atualizada"));

        if (unitDto.name() != null) unit.setName(unitDto.name());
        if (unitDto.responsible_id() != null) {
            User responsible = userRepository.findById(unitDto.responsible_id())
                    .orElseThrow(() -> new NotFoundException("Usuário responsável não encontrado"));
            unit.setResponsible(responsible);
        }
        if (unitDto.phone() != null) unit.setPhone(unitDto.phone());
        if (unitDto.email() != null) unit.setEmail(unitDto.email());
        if (unitDto.active() != null) unit.setActive(unitDto.active());

        if (unitDto.address() != null) {
            AddressRequestDto a = unitDto.address();
            Address address = unit.getAddress();

            address.setNumber(a.number());
            address.setStreet(a.street());
            address.setDistrict(a.district());
            address.setCity(a.city());
            address.setState(a.state());
            address.setZipCode(a.zipCode());
            address.setComplement(a.complement());

        }

        return unitRepository.save(unit);
    }

    @Transactional
    public Unit deleteUnit(UUID id) {
        Unit unit = unitRepository.findById(id).orElseThrow(() -> new NotFoundException("Unidade não encontrada para ser deletada"));
        unitRepository.delete(unit);
        return unit;
    }

    public UnitStatsResponseDto getUnitStats() {
        PreStatsUnitDto preStatsUnit = unitRepository.getCountUnits();
        UnitResponseDto unit = new UnitResponseDto(unitRepository.findLastInsert().orElseThrow(() -> new NotFoundException("Nenhuma Unidade adicionada!")));
        return new UnitStatsResponseDto(preStatsUnit.totalUnits(), preStatsUnit.unitsActives(), preStatsUnit.unitsDeactivates(), unit);
    }
}