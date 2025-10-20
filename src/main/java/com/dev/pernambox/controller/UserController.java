package com.dev.pernambox.controller;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.UserRequestDto;
import com.dev.pernambox.domain.user.dtos.UserResponseDto;
import com.dev.pernambox.domain.user.dtos.UserUpdateDto;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.exceptions.UpdateEntityException;
import com.dev.pernambox.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createNewUser(@Valid @RequestBody UserRequestDto body, Authentication authentication, UriComponentsBuilder uriComponentsBuilder) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.USER) {
            throw new SecurityException("Você não tem Autorização para criar usuários!");
        }

        User newUser = userService.save(body);

        URI uri = uriComponentsBuilder.path("/user/info/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserResponseDto(newUser));
    }

    @GetMapping("info/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@org.hibernate.validator.constraints.UUID @PathVariable UUID userId) {
        return ResponseEntity.ok(new UserResponseDto(userService.getUserById(userId)));
    }

    @GetMapping("/all-in-unit/{unitId}")
    public ResponseEntity<List<UserResponseDto>> getAllUsersByUnitId(@PathVariable UUID unitId) {
        return ResponseEntity.ok(userService.getAllUsersByUnitId(unitId).stream().map(UserResponseDto::new).toList());
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(UserResponseDto::new).toList());
    }

    @PutMapping("/update/me")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (!user.getId().equals(body.userId())) {
            throw new SecurityException("Não é possível alterar outro usuário por esta rota!");
        }

        return ResponseEntity.ok(new UserResponseDto(userService.update(body)));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<UserResponseDto> adminUpdateUser(@Valid @RequestBody UserUpdateDto body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.USER) {
            if (!user.getId().equals(body.userId())) {
                throw new SecurityException("Você não tem Autorização para alterar outro usuário");
            }
        }

        return ResponseEntity.ok(new UserResponseDto(userService.update(body)));
    }
}
