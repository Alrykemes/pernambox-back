package com.dev.pernambox.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Polo")
@Table(name = "polo", schema = "public")
public class Polo {
    @Id
    @Column(name = "id",  nullable = false,  unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome",  nullable = false,  unique = true)
    private String nome;

    @Column(name = "numero",  nullable = false,  unique = true)
    private String numero;

    @Column(name = "bairro",  nullable = false,  unique = true)
    private String bairro;

    @Column(name = "municipio",  nullable = false,  unique = true)
    private String municipio;

    @Column(name = "cep",  nullable = false,  unique = true)
    private String cep;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "users")
    private List<User> users;

}
