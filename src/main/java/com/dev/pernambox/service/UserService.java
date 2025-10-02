package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User changeUserPassword(UUID userId, String password) {
        return this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password)).orElseThrow(() -> new RuntimeException("Error changing password"));
    }
}
