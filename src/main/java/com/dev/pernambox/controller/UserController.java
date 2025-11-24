package com.dev.pernambox.controller;

import com.dev.pernambox.domain.loghistory.dtos.LogHistoryWithoutUnitDto;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryTarget;
import com.dev.pernambox.domain.loghistory.enums.LogHistoryType;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.*;
import com.dev.pernambox.domain.user.enums.Role;
import com.dev.pernambox.exceptions.AuthorizationException;
import com.dev.pernambox.exceptions.DeleteEntityException;
import com.dev.pernambox.service.EmailService;
import com.dev.pernambox.service.LogHistoryService;
import com.dev.pernambox.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "Operações relacionadas aos usuários de forma geral")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final LogHistoryService logHistoryService;

    @PostMapping("/create")
    @Operation(summary = "Cria usuário", description = "Um usuário admin cria um acesso para outro usuário, novo usuário recebe email de boas vindas e instruções de primeiro acesso.")
    public ResponseEntity<UserResponseDto> createNewUser(@Valid @RequestBody UserRequestDto body, Authentication authentication, UriComponentsBuilder uriComponentsBuilder) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.USER) {
            throw new AuthorizationException("Você não tem Autorização para criar usuários!");
        }

        if (body.role().equals(Role.ADMIN) && !user.getRole().equals(Role.ADMIN_MASTER)) {
            throw new AuthorizationException("Apenas Master Admins podem criar outros Admins!");
        }

        User newUser = userService.save(body);

        logHistoryService.createLogHistory(new LogHistoryWithoutUnitDto(
                LogHistoryType.CREATE,
                LogHistoryTarget.USER,
                this.getDescriptionOperation(LogHistoryType.CREATE, user, newUser),
                newUser.getId(),
                user.getId()
        ));

        emailService.sendEmail(
                newUser.getEmail(),
                "Seja bem vindo(a) ao Pernambox",
                "Olá " + newUser.getName() + " Seu cadastro no Pernambox foi realizado pelo(a) " + user.getName() + "\n"
                        + "OBS: Antes de entrar faça o procedimento de troca de senha para fazer seu primeiro login."
        );

        URI uri = uriComponentsBuilder.path("/user/info/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserResponseDto(newUser));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deleta um usuário", description = "Deleta o usuário")
    public void deleteUser(@PathVariable UUID userId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole() == Role.USER || user.getRole() == Role.ADMIN && !user.getId().equals(userId)) {
            throw new DeleteEntityException("Você não tem permissão para deletar usuários ou a si mesmo!");
        }

        User userDeleted = userService.deleteUser(userId);

        logHistoryService.createLogHistory(new LogHistoryWithoutUnitDto(
                LogHistoryType.DELETE,
                LogHistoryTarget.USER,
                this.getDescriptionOperation(LogHistoryType.DELETE, user, userDeleted),
                userDeleted.getId(),
                user.getId()
        ));
    }

    @GetMapping("/info/{userId}")
    @Operation(summary = "Retorna usuário pelo id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(new UserResponseDto(userService.getUserById(userId)));
    }

    @GetMapping("/all/admins-masters")
    @Operation(summary = "Retorna todos os usuários admins masters")
    public ResponseEntity<List<UserResponseDto>> getUsersAdminsMasters() {
        return ResponseEntity.ok(userService.getAllUsersAdminsMasters().stream().map(UserResponseDto::new).toList());
    }

    @GetMapping("/all/admins")
    @Operation(summary = "Retorna todos os usuários admins")
    public ResponseEntity<List<UserResponseDto>> getUsersAdmins() {
        return ResponseEntity.ok(userService.getAllUsersAdmins().stream().map(UserResponseDto::new).toList());
    }

    @GetMapping("/stats")
    @Operation(summary = "Status de todos usuários")
    public ResponseEntity<StatsUsersResponseDto> getStatsUsers() {
        return ResponseEntity.ok(this.userService.getUsersStats());
    }

    @GetMapping()
    @Operation(summary = "Retorna usuário pelo nome, com possibilidade de inserir filtros")
    public ResponseEntity<PageUserResponseDto> getUserByName(
            @RequestParam() String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean active,
            @RequestParam(required = false, defaultValue = "false") boolean noActive,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAdmin,
            @RequestParam(required = false, defaultValue = "false") boolean onlyUser) {
        return ResponseEntity.ok(
                new PageUserResponseDto(
                        userService.getUserByName(
                                name,
                                (page - 1),
                                size,
                                active,
                                noActive,
                                onlyAdmin,
                                onlyUser
                        )
                )
        );
    }

    @GetMapping("/all")
    @Operation(summary = "Retorna todos usuários de forma paginada, com possibilidade de inserir filtros")
    public ResponseEntity<PageUserResponseDto> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new PageUserResponseDto(userService.getAllUsers((page - 1), size)));
    }


    @PutMapping("/update/me")
    @Operation(summary = "Atualiza usuário", description = "Rota utilizada pelo usuário para alterar suas próprias informações.")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (!user.getId().equals(body.userId())) {
            throw new AuthorizationException("Não é possível alterar outro usuário por esta rota!");
        }

        logHistoryService.createLogHistory(new LogHistoryWithoutUnitDto(
                LogHistoryType.UPDATE,
                LogHistoryTarget.USER,
                this.getDescriptionOperation(LogHistoryType.UPDATE, user, user),
                user.getId(),
                user.getId()
        ));

        return ResponseEntity.ok(new UserResponseDto(userService.update(body, user)));
    }

    @PutMapping("/admin/update")
    @Operation(summary = "Admin atualiza usuário", description = "Rota utilizada por um ADMIN para alterar informações de outro usuário.")
    public ResponseEntity<UserResponseDto> adminUpdateUser(@Valid @RequestBody UserUpdateDto body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (user.getRole() == Role.USER) {
            if (!user.getId().equals(body.userId())) {
                throw new AuthorizationException("Você não tem Autorização para alterar outro usuário");
            }
        }

        User userUpdated = userService.update(body, user);

        logHistoryService.createLogHistory(new LogHistoryWithoutUnitDto(
                LogHistoryType.UPDATE,
                LogHistoryTarget.USER,
                this.getDescriptionOperation(LogHistoryType.UPDATE, user, userUpdated),
                userUpdated.getId(),
                user.getId()
        ));

        return ResponseEntity.ok(new UserResponseDto(userUpdated));
    }

    private String getDescriptionOperation(LogHistoryType type, User userResponsible, User userAffected) {
        return type.equals(LogHistoryType.CREATE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Criou o usuario " + userAffected.getName() + " de id " + userAffected.getId().toString()
                : type.equals(LogHistoryType.UPDATE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Atualizou o usuario " + userAffected.getName() + " de id " + userAffected.getId().toString()
                : type.equals(LogHistoryType.DELETE) ?
                "O usuário " + userResponsible.getName() + " de id " + userResponsible.getId().toString()
                        + " Deletou o usuario " + userAffected.getName() + " de id " + userAffected.getId().toString()
                : "Tipo de Operação Inválido";
    }

}
