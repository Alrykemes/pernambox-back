package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserById(String userId) {
        return this.userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void changeUserPassword(UUID userId, String password) {
        this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password));
    }
}
