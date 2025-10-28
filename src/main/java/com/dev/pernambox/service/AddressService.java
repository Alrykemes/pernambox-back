package com.dev.pernambox.service;

import com.dev.pernambox.domain.address.Address;
import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import com.dev.pernambox.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public Address addAddress(AddressRequestDto addressDto) {

        try {
            Address address = new Address(addressDto);

            if (!verifyZipCode(address.getZipCode())) {
                throw new Exception("This zip code is invalid");
            }

            return addressRepository.save(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Address editAddress(Address address) {
        try {
            Address oldAddress = addressRepository.findById(address.getId()).get();

            if (oldAddress == null) {
                throw new Exception("This address does not exist");
            }

            if (oldAddress == address) {
                throw new Exception("Cannot change the Address with the same fields");
            }

            if (!verifyZipCode(address.getZipCode())) {
                throw new Exception("This zip code is invalid");
            }

            return addressRepository.save(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteAddress(UUID addressId) {
        Optional<Address> oldAddress = addressRepository.findById(addressId);

        if (oldAddress.isPresent()) {
            addressRepository.delete(oldAddress.get());
            return true;
        }
        return false;
    }

    public boolean verifyZipCode(String zipCode) {
        if (zipCode == null) {
            return false;
        }

        // Remove espaços e mantém apenas o texto limpo
        String cleaned = zipCode.trim();

        // Aceita os dois formatos válidos de CEP:
        // - "12345678"  (8 dígitos)
        // - "12345-678" (com hífen)
        return cleaned.matches("\\d{8}") || cleaned.matches("\\d{5}-\\d{3}");
    }
}
