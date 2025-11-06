package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.origin.Origin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OriginRepository extends JpaRepository<Origin, UUID> {

}
