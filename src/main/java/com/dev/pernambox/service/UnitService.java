package com.dev.pernambox.service;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.UnitRequestDto;
import com.dev.pernambox.repositories.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UnitService {
    private final UnitRepository unitRepository;

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Unit saveUnit(UnitRequestDto unitDto) {
        try
        {
            Unit unit = new Unit();
            unit.setName(unitDto.name());
            unit.setAddress(unitDto.address());

            // Verifica se já existe uma outra unidade com o mesmo nome
            if (unitRepository.findByName(unit.getName()) != null) {
                throw new Exception("There is already a unit with the same name");
            }
            return unitRepository.save(unit);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Unit updateUnit(Unit unit) {
        try{
            Unit oldUnit = unitRepository.findUnitById(unit.getId());

            // Verifica se a unidade já existe e se foi inalterada
            if (oldUnit == null) {
                throw new Exception("The unit with the same id does not exist");
            }
            if(oldUnit == unit){
                throw new Exception("Cannot change the unit with the same fields");
            }

            return unitRepository.save(unit);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUnit(UUID idUnit) {
        try{
            Unit oldUnit = unitRepository.findUnitById(idUnit);

            if (oldUnit == null) {
                throw new Exception("The unit with the same id does not exist");
            }
            else {
                unitRepository.delete(oldUnit);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
