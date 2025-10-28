package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.UserRequestDto;
import com.dev.pernambox.domain.user.dtos.UserUpdateDto;
import com.dev.pernambox.exceptions.AuthorizationException;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.exceptions.PasswordResetException;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final UnitService unitService;


    public User save(UserRequestDto userRequestDto) {
        User newUser = new User(userRequestDto);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        return this.userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<User> getUserByName(String name) {
        return this.userRepository.getUserByName(name);
    }

    public boolean changeUserPassword(UUID userId, String password) {
        return this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password)) > 0;
    }

    public void verifySamePassword(UUID userId, String password) {
        User user = this.getUserById(userId);

        if(passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordResetException("A nova senha não pode ser igual a antiga senha!");
        }
    }

    public User update(UserUpdateDto updateDto) {
        User user = this.getUserById(updateDto.userId());

        if(!passwordEncoder.matches(updateDto.password(), user.getPassword())) {
            throw new AuthorizationException("Senha Incorreta!");
        }

        setUpdateValues(updateDto, user);

        if(updateDto.newPassword().equals(updateDto.password())) {
            throw new UpdateEntityException("A nova senha não pode ser ingual a antiga!");
        }

        return this.userRepository.save(user);
    }

    private void setUpdateValues(UserUpdateDto updateDto, User user) {
        user.setName(updateDto.name() == null ? user.getName() : updateDto.name());
        user.setEmail(updateDto.email() == null ? user.getEmail() : updateDto.email());
        user.setRole(updateDto.role() == null ? user.getRole() : updateDto.role());
        user.setCpf(updateDto.cpf() == null ? user.getCpf() : updateDto.cpf());
        user.setPhone(updateDto.phone() == null ? user.getPhone() : updateDto.phone());
        user.setPassword(updateDto.newPassword() == null ? user.getPassword() : passwordEncoder.encode(updateDto.newPassword()));
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
}
