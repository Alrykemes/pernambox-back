package com.dev.pernambox.domain;

import com.dev.pernambox.domain.enums.Permissao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "usuario", schema = "public")
public class User implements UserDetails {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefone", nullable = false, unique = true)
    private String telefone;

    @Column(name = "senha", nullable = false, unique = true)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "permissao", columnDefinition = "permissao", nullable = false)
    private Permissao permissao;

    @ManyToOne
    @JoinColumn(name = "polo_id")
    private Polo polo;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.permissao == Permissao.ADM_GERAL) {
            return List.of(new SimpleGrantedAuthority("ADM_GERAL"), new SimpleGrantedAuthority("ADM_POLO"), new SimpleGrantedAuthority("FUNCIONARIO"));
        } else if(this.permissao == Permissao.ADM_POLO) {
            return List.of(new SimpleGrantedAuthority("ADM_POLO"), new SimpleGrantedAuthority("FUNCIONARIO"));
        } else {
            return List.of(new SimpleGrantedAuthority("FUNCIONARIO"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
