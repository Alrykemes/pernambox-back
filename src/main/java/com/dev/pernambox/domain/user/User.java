package com.dev.pernambox.domain.user;

import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.domain.unit.Unit;
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
@Table(name = "users", schema = "public")
public class User implements UserDetails {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "role", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.MASTER_ADM) {
            return List.of(new SimpleGrantedAuthority("MASTER_ADM"), new SimpleGrantedAuthority("UNIT_ADM"), new SimpleGrantedAuthority("USER"));
        } else if(this.role == Role.UNIT_ADM) {
            return List.of(new SimpleGrantedAuthority("UNIT_ADM"), new SimpleGrantedAuthority("USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
