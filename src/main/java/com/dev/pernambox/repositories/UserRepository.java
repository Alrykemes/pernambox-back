package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query()
    Optional<User> findByEmail(String email);
}
