package com.dev.pernambox.service;

import com.dev.pernambox.domain.address.dtos.AddressRequestDto;
import com.dev.pernambox.domain.unit.Unit;
import com.dev.pernambox.domain.unit.dtos.UnitCreateRequestDto;
import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.exceptions.NotFoundException;
import com.dev.pernambox.repositories.UnitRepository;
import com.dev.pernambox.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    @InjectMocks
    private UnitService unitService;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Unit> unitCaptor;

    @Test
    void shouldCreateUnitWhenDataIsValid() {
        UUID responsibleId = UUID.randomUUID();
        AddressRequestDto addressDto = new AddressRequestDto(
                "123", "Rua Mandacaru", "Paulista", "Xique-Xique", "PE", "12345-678", "Apt 1"
        );
        UnitCreateRequestDto requestDto = new UnitCreateRequestDto(
                "Unidade Olinda",
                responsibleId,
                "(81) 99999-9999",
                "olinda@defesa.br",
                addressDto
        );

        User responsibleUser = new User();
        when(userRepository.findById(responsibleId)).thenReturn(Optional.of(responsibleUser));
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> {
            Unit u = invocation.getArgument(0);
            if (u.getId() == null) {
                u.setId(UUID.randomUUID());
            }
            return u;
        });

        Unit created = unitService.saveUnit(requestDto, null);

        verify(unitRepository, times(1)).save(unitCaptor.capture());
        Unit saved = unitCaptor.getValue();

        assertNotNull(saved);
        assertEquals("Unidade Olinda", saved.getName());
        assertEquals("(81) 99999-9999", saved.getPhone());
        assertEquals("olinda@defesa.br", saved.getEmail());
        assertTrue(saved.getActive());
        assertNotNull(saved.getAddress(), "Address should be set from DTO");
        assertEquals(responsibleUser, saved.getResponsible(), "Responsible user should be set from repository");
        assertNotNull(created.getId());
    }

    @Test
    void shouldThrowNotFoundWhenResponsibleNotFound() {
        UUID responsibleId = UUID.randomUUID();
        AddressRequestDto addressDto = new AddressRequestDto(
                "123", "Main Street", "Central", "CityName", "ST", "12345-678", "Apt 1"
        );
        UnitCreateRequestDto requestDto = new UnitCreateRequestDto(
                "Unidade Olinda",
                responsibleId,
                "(81) 99999-9999",
                "olinda@defesa.br",
                addressDto
        );

        when(userRepository.findById(responsibleId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            unitService.saveUnit(requestDto, null);
        });

        assertTrue(ex.getMessage().toLowerCase().contains("responsável") || ex.getMessage().toLowerCase().contains("responsavel"));
        verify(unitRepository, never()).save(any(Unit.class));
    }

    @Test
    void shouldReturnThreeUnitsWhenUnitsExist() {
        Unit unit1 = new Unit();
        unit1.setId(UUID.randomUUID());
        unit1.setName("Unidade Olinda");

        Unit unit2 = new Unit();
        unit2.setId(UUID.randomUUID());
        unit2.setName("Unidade Recife");

        Unit unit3 = new Unit();
        unit3.setId(UUID.randomUUID());
        unit3.setName("Unidade Jaboatão");

        when(unitRepository.findAll()).thenReturn(List.of(unit1, unit2, unit3));

        List<Unit> result = unitService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size(), "Should return exactly 3 units");
        assertEquals("Unidade Olinda", result.get(0).getName());
        assertEquals("Unidade Recife", result.get(1).getName());
        assertEquals("Unidade Jaboatão", result.get(2).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoUnitsExist() {
        when(unitRepository.findAll()).thenReturn(List.of());

        List<Unit> result = unitService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return an empty list when no units exist");
    }
}