package com.dev.pernambox.service;

import com.dev.pernambox.domain.resource.Resource;
import com.dev.pernambox.domain.resource.dtos.ResourceRequestDto;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.ResourceRepository;
import com.dev.pernambox.repositories.UnitRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final UnitRepository unitRepository;

    @Transactional
    public void saveResource(ResourceRequestDto resourceDto) {
        Resource resource = new Resource(resourceDto);

        Unit unit = unitRepository.findById(resource.getUnit_id().getId()).orElseThrow(() -> {
            throw new NotFoundException("Unit not found");
        });


    }
}
