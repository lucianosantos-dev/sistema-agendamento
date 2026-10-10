package com.lucianodev.sistemaagendamento.service;


import com.lucianodev.sistemaagendamento.dto.request.ProfissionalCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ProfissionalUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ProfissionalResponse;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.factory.ProfissionalFactory;
import com.lucianodev.sistemaagendamento.mapper.ProfissionalMapper;
import com.lucianodev.sistemaagendamento.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfissionalTest {

    @Mock
    private ProfissionalRepository repository;
    @Mock
    private ProfissionalMapper mapper;
    @InjectMocks
    private ProfissionalService service;

    private Profissional profissional;
    private ProfissionalUpdateDto updateDto;
    private ProfissionalResponse profissionalResponse;
    private ProfissionalCreateDto createDto;

    @BeforeEach
    public void setUp() {
        profissional = ProfissionalFactory.createProfissional();
        updateDto = ProfissionalFactory.profissionalUpdateDto();
        createDto = ProfissionalFactory.profissionalCreateDto();
        profissionalResponse = ProfissionalFactory.profissionalResponse();
    }

    @Test
    public void deveLancarConflictException_QuandoTentarCadastrarProfissional() {
        when(repository.existsByNome(profissional.getNome())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.create(createDto));

        verify(repository, times(1)).existsByNome(profissional.getNome());
        verify(repository, never()).save(any());
    }

    @Test
    public void deveCriarProfissionalComSucesso() {
        when(repository.existsByNome(profissional.getNome())).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(profissional);
        when(repository.save(profissional)).thenReturn(profissional);
        when(mapper.toResponse(profissional)).thenReturn(profissionalResponse);

        ProfissionalResponse response = service.create(createDto);

        assertNotNull(response);
        assertEquals(profissional.getId(), response.id());

        InOrder inOrder = inOrder(repository, mapper);

        inOrder.verify(repository).existsByNome(profissional.getNome());
        inOrder.verify(mapper).toEntity(createDto);
        inOrder.verify(repository).save(profissional);
        inOrder.verify(mapper).toResponse(profissional);

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtualizarProfissional() {
        when(repository.findById(profissional.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(profissional.getId(), updateDto));

        verify(repository, times(1)).findById(profissional.getId());
        verify(repository, never()).save(any());
    }

    @Test
    public void deveAtualizarProfissionalComSucesso() {
        when(repository.findById(profissional.getId())).thenReturn(Optional.of(profissional));
        when(repository.save(profissional)).thenReturn(profissional);
        when(mapper.toResponse(profissional)).thenReturn(profissionalResponse);

        ProfissionalResponse response = service.update(profissional.getId(), updateDto);
        assertNotNull(response);
        assertEquals(profissional.getId(), response.id());

        InOrder inOrder = inOrder(repository, mapper);

        inOrder.verify(repository).findById(profissional.getId());
        inOrder.verify(mapper).update(updateDto, profissional);
        inOrder.verify(repository).save(profissional);
        inOrder.verify(mapper).toResponse(profissional);

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarProfissionalPeloId() {
        when(repository.findById(profissional.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(profissional.getId()));

        verify(repository, times(1)).findById(profissional.getId());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    public void deveBuscarProfissionalPeloIdComSucesso() {
        when(repository.findById(profissional.getId())).thenReturn(Optional.of(profissional));
        when(mapper.toResponse(profissional)).thenReturn(profissionalResponse);

        ProfissionalResponse response = service.findById(profissional.getId());
        assertNotNull(response);
        assertEquals(profissional.getId(), response.id());

        InOrder inOrder = inOrder(repository, mapper);

        inOrder.verify(repository).findById(profissional.getId());
        inOrder.verify(mapper).toResponse(profissional);

        verifyNoMoreInteractions(repository, mapper);
    }
}
