package com.dev.pernambox.domain.address.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
        @NotBlank(message = "The number field is required")
        String number,

        @NotBlank(message = "The street field is required")
        String street,

        @NotBlank(message = "The district field is required")
        String district,

        @NotBlank(message = "The city field is required")
        String city,

        @NotBlank(message = "The state field is required")
        String state,

        @NotBlank(message = "The zipCode field is required")
        @Size(min = 8, max = 8, message = "the postcode can only be 8 characters long")
        String zipCode,

        @NotBlank(message = "The complement field is required")
        String complement
) {}
