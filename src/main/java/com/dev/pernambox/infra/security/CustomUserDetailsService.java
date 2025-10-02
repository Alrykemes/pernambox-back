package com.dev.pernambox.infra.security;

import com.dev.pernambox.repositories.UserRepository;
import com.dev.pernambox.domain.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Username é o email so ta com este nome para poder sobrescrever o metodo da classe
        // UserDetailsService
        return userRepository.findByEmail(username)
                .orElseThrow((() -> new UsernameNotFoundException("User not found\nUserID:" + username)));
    }
}