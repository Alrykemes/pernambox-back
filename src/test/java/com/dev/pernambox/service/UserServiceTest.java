package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.UserUpdateDto;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldAllowUserToEditTheirOwnProfile() {
        UUID userId = UUID.randomUUID();

        // Usuário existente no banco
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Usuário Antigo");
        existingUser.setPassword("senha-criptografada");
        existingUser.setRole(Role.USER);

        User responsibleUser = new User();
        responsibleUser.setId(userId);
        responsibleUser.setRole(Role.USER);

        UserUpdateDto updateDto = new UserUpdateDto(
                userId,
                "Usuário Atualizado",
                null,
                null,
                null,
                null,
                "senha-atual",
                true,
                null,
                "nova-senha"
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches("senha-atual", "senha-criptografada"))
                .thenReturn(true);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.update(updateDto, responsibleUser);

        assertNotNull(updatedUser);
        assertEquals("Usuário Atualizado", updatedUser.getName());

        verify(userRepository).save(existingUser);
        verify(passwordEncoder).matches("senha-atual", "senha-criptografada");
    }
}