package com.dev.pernambox.domain.address;

import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "Address")
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String complement;

    public Address(AddressRequestDto dto) {
        this.number = dto.number();
        this.street = dto.street();
        this.district = dto.district();
        this.city = dto.city();
        this.state = dto.state();
        this.zipCode = dto.zipCode();
        this.complement = dto.complement();
    }

    public Address() {

    }
}
