package com.dev.pernambox.service;

import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.UserRequestDto;
import com.dev.pernambox.domain.user.dtos.UserUpdateDto;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.exceptions.PasswordResetException;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.repositories.UserRepository;
import lombok.AllArgsConstructor;
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

        // pegar a unidade verificando se é válida pra depois jogar pro service
//        Unit unit = unitService.getUnitById(body.unit_Id());

        return this.userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean changeUserPassword(UUID userId, String password) {
        return this.userRepository.updatePasswordById(userId, passwordEncoder.encode(password)) > 0;
    }

    public void verifySamePassword(UUID userId, String password) {
        User user = this.getUserById(userId);

        if(passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordResetException("New password cannot be the same as the old password");
        }
    }

    public User updateByAdmin(UserUpdateDto updateDto) {
        User user = this.getUserById(updateDto.userId());
        setUpdateValues(updateDto, user);
        user.setUnit(updateDto.unitId() == null ? user.getUnit() : /*pega do unit service*/ user.getUnit());

        return this.userRepository.save(user);
    }

    public User update(UserUpdateDto updateDto) {
        User user = this.getUserById(updateDto.userId());

        if(!passwordEncoder.matches(updateDto.password(), user.getPassword())) {
            throw new UpdateEntityException("Senha Incorreta!");
        }

        setUpdateValues(updateDto, user);

        return this.userRepository.save(user);
    }

    private void setUpdateValues(UserUpdateDto updateDto, User user) {
        user.setName(updateDto.name() == null ? user.getName() : updateDto.name());
        user.setEmail(updateDto.email() == null ? user.getEmail() : updateDto.email());
        user.setRole(updateDto.role() == null ? user.getRole() : updateDto.role());
        user.setCpf(updateDto.cpf() == null ? user.getCpf() : updateDto.cpf());
        user.setPhone(updateDto.phone() == null ? user.getPhone() : updateDto.phone());
        user.setPassword(updateDto.password() == null ? user.getPassword() : passwordEncoder.encode(updateDto.password()));
    }

    public List<User> getAllUsersByUnitId(UUID unitId) {
        return userRepository.getAllByUnitId(unitId);
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
}
