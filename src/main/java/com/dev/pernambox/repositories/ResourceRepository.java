package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
}
