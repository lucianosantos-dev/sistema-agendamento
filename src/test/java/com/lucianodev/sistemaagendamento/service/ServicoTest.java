package com.lucianodev.sistemaagendamento.service;


import com.lucianodev.sistemaagendamento.dto.request.ServicoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ServicoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ServicoResponse;
import com.lucianodev.sistemaagendamento.entity.Servico;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.factory.ServicoFactory;
import com.lucianodev.sistemaagendamento.mapper.ServicoMapper;
import com.lucianodev.sistemaagendamento.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicoTest {

    @Mock
    private ServicoRepository repository;
    @InjectMocks
    private ServicoService servicoService;
    @Mock
    private ServicoMapper servicoMapper;

    private Servico servico;
    private ServicoCreateDto servicoCreateDto;
    private ServicoResponse servicoResponse;
    private ServicoUpdateDto servicoUpdateDto;

    @BeforeEach
    public void setUp() {
        servico = ServicoFactory.servicoEntity();
        servicoCreateDto = ServicoFactory.servicoCreateDto();
        servicoResponse = ServicoFactory.servicoResponse();
        servicoUpdateDto = ServicoFactory.servicoUpdateDto();
    }

    // Lança exceção quando existe um serviço com nome específico//
    @Test
    public void deveLancarConflictException_QuandoTentarCadastarServicoComNomeExistente() {
        when(repository.existsByNome(servico.getNome())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> servicoService.create(servicoCreateDto));

        verify(repository, times(1)).existsByNome(servico.getNome());
        verify(repository, never()).save(any());
    }

    @Test
    public void deveCriarServicoComSucesso() {
        when(repository.existsByNome(servico.getNome())).thenReturn(false);
        when(servicoMapper.toEntity(servicoCreateDto)).thenReturn(servico);
        when(repository.save(servico)).thenReturn(servico);
        when(servicoMapper.toResponse(servico)).thenReturn(servicoResponse);

        ServicoResponse response = servicoService.create(servicoCreateDto);

        assertNotNull(response);
        assertEquals(servico.getId(), response.id());

        verify(repository, times(1)).existsByNome(servico.getNome());
        verify(repository, times(1)).save(servico);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtualizarComIdInexistente() {
        when(repository.findById(servico.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> servicoService.update(servico.getId(), servicoUpdateDto));

        verify(repository, times(1)).findById(servico.getId());
        verify(repository, never()).save(any());
    }


    // Unidade de nome garantida pela Constraint UNIQUE no banco migration(V1).
    // Conflito tratado pelo GlobalExceptionHandler via DataIntegrityViolationException.
    @Test
    public void deveAtualizarServicoComSucesso() {
        when(repository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(repository.save(servico)).thenReturn(servico);
        when(servicoMapper.toResponse(servico)).thenReturn(servicoResponse);

        ServicoResponse response = servicoService.update(servico.getId(), servicoUpdateDto);
        assertNotNull(response);
        assertEquals(servico.getId(), response.id());

        verify(repository, times(1)).findById(servico.getId());
        verify(servicoMapper, times(1)).updateEntity(servicoUpdateDto, servico);
        verify(repository, times(1)).save(servico);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarServicoComIdInexistente() {
        when(repository.findById(servico.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> servicoService.findById(servico.getId()));

        verify(repository, times(1)).findById(servico.getId());
        verify(servicoMapper, never()).toResponse(any());
    }

    @Test
    public void deveBuscarServicoPeloIdComSucesso() {
        when(repository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(servicoMapper.toResponse(servico)).thenReturn(servicoResponse);

        ServicoResponse response = servicoService.findById(servico.getId());

        assertNotNull(response);
        assertEquals(servico.getId(), response.id());

        verify(repository, times(1)).findById(servico.getId());
        verify(servicoMapper, times(1)).toResponse(servico);
    }
}
