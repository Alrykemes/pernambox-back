package com.dev.pernambox.service;

import com.dev.pernambox.repositories.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
}
