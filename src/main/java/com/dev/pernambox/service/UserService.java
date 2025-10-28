package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.exceptions.PasswordResetException;
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
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserById(String userId) {
        return this.userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean changeUserPassword(UUID userId, String password) {
        return this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password)) > 0;
    }

    public void verifySamePassword(UUID userId, String password) {
        User user = this.getUserById(userId.toString());

        if(passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordResetException("A nova senha não pode ser igual a antiga senha!");
        }
    }
}
