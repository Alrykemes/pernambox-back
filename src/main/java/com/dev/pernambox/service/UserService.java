package com.dev.pernambox.service;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.UserRequestDto;
import com.dev.pernambox.domain.user.dtos.StatsUsersResponseDto;
import com.dev.pernambox.domain.user.dtos.UserUpdateDto;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.exceptions.*;
import com.dev.pernambox.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(UserRequestDto userRequestDto) {
        if (this.userRepository.existsByCpfEquals(userRequestDto.cpf())) {
            throw new CreateEntityException("Já existe um cadastro com este CPF!");
        }
        if (this.userRepository.existsByPhoneEquals(userRequestDto.phone())) {
            throw new CreateEntityException("Já existe um cadastro com este Telefone!");
        }
        if (this.userRepository.existsByEmailEquals(userRequestDto.email())) {
            throw new CreateEntityException("Já existe um cadastro com este Email!");
        }
//        if(CpfUtils.isValidCPF(userRequestDto.cpf())) {
//            throw new CreateEntityException("Número de CPF inválido!");
//        }

        User newUser = new User(userRequestDto);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        return this.userRepository.save(newUser);
    }

    public User deleteUser(UUID userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        this.userRepository.deleteById(userId);

        return user;
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Page<User> getUserByName(String name, int page, int size, boolean active,
                                    boolean deactive, boolean onlyAdmins, boolean onlyUsers) {
        System.out.println(page);
        Pageable pageable = PageRequest.of(page, size);

        return this.userRepository.getUsersByNameWithFilter(name, active, deactive, onlyUsers, onlyAdmins, pageable);
    }

    public boolean changeUserPassword(UUID userId, String password) {
        return this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password)) > 0;
    }

    public void verifySamePassword(UUID userId, String password) {
        User user = this.getUserById(userId);

        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordResetException("A nova senha não pode ser igual a antiga senha!");
        }
    }

    public User update(UserUpdateDto updateDto, User responsibleUser) {
        User user = this.getUserById(updateDto.userId());
        if (responsibleUser.getRole().equals(Role.USER)) {
            if (!passwordEncoder.matches(updateDto.password(), user.getPassword())) {
                throw new AuthorizationException("Senha Incorreta!");
            }
            if (updateDto.newPassword() != null) {
                if (updateDto.newPassword().equals(updateDto.password())) {
                    throw new UpdateEntityException("A nova senha não pode ser igual a antiga!");
                }
            }
        }
        if (responsibleUser.getRole().equals(Role.ADMIN) && user.getRole().equals(Role.ADMIN_MASTER)) {
            throw new AuthorizationException("Você não tem permissão para alterar um Administrador Geral!");
        }

        setUpdateValues(updateDto, user);

        return this.userRepository.save(user);
    }

    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.getAll(pageable);
    }

    public List<User> getAllUsersAdminsMasters() {
        return userRepository.getAllAdminsMasters();
    }

    public List<User> getAllUsersAdmins() {
        return userRepository.getAllAdmins();
    }

    public StatsUsersResponseDto getUsersStats() {
        return this.userRepository.getUsersStats();
    }

    private void setUpdateValues(UserUpdateDto updateDto, User user) {
        if (updateDto.email() != null && this.userRepository.existsByEmailAndIdNot(updateDto.email(), updateDto.userId())) {
            throw new UpdateEntityException("Já existe um cadastro com este Email!");
        }
        if (updateDto.phone() != null && this.userRepository.existsByPhoneAndIdNot(updateDto.phone(), updateDto.userId())) {
            throw new UpdateEntityException("Já existe um cadastro com este Telefone!");
        }
        if (updateDto.cpf() != null && this.userRepository.existsByCpfAndIdNot(updateDto.cpf(), updateDto.userId())) {
            throw new UpdateEntityException("Já existe um cadastro com este CPF!");
        }
//        else if(CpfUtils.isValidCPF(updateDto.cpf())) {
//            throw new UpdateEntityException("Número de CPF inválido!");
//        }

        user.setName(updateDto.name() == null ? user.getName() : updateDto.name());
        user.setEmail(updateDto.email() == null ? user.getEmail() : updateDto.email());
        user.setRole(updateDto.role() == null ? user.getRole() : updateDto.role());
        user.setCpf(updateDto.cpf() == null ? user.getCpf() : updateDto.cpf());
        user.setPhone(updateDto.phone() == null ? user.getPhone() : updateDto.phone());
        user.setActive(updateDto.active() == null ? user.getActive() : updateDto.active());
        user.setImageProfile(updateDto.imageProfile() == null ? user.getImageProfile() : updateDto.imageProfile());
        user.setPassword(updateDto.newPassword() == null ? user.getPassword() : passwordEncoder.encode(updateDto.newPassword()));
    }
}
