package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findById(UUID id);

    Address save(Address address);

    void delete(Address address);
}
