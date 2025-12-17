package com.dev.pernambox.domain.user.dtos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NewPasswordRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void mustAcceptValidPassword() {
        NewPasswordRequestDto dto =
                new NewPasswordRequestDto("SenhaValida1!");

        Set<ConstraintViolation<NewPasswordRequestDto>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty(), "Senha válida não deveria gerar erros");
    }

    @Test
    void mustRejectPasswordWithLessThan8Characters() {
        NewPasswordRequestDto dto =
                new NewPasswordRequestDto("Ab1!");

        Set<ConstraintViolation<NewPasswordRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("mínimo 8")));
    }

    @Test
    void mustRejectPasswordWithoutNumber() {
        NewPasswordRequestDto dto =
                new NewPasswordRequestDto("SenhaSemNumero!");

        Set<ConstraintViolation<NewPasswordRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("número")));
    }

    @Test
    void mustRejectPasswordWithoutSpecialCharacter() {
        NewPasswordRequestDto dto =
                new NewPasswordRequestDto("SenhaSemEspecial1");

        Set<ConstraintViolation<NewPasswordRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("caractere especial")));
    }

    @Test
    void mustRejectPasswordLargerThan255Characters() {
        String password = "A1!".repeat(100);

        NewPasswordRequestDto dto =
                new NewPasswordRequestDto(password);

        Set<ConstraintViolation<NewPasswordRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("255")));
    }
}
