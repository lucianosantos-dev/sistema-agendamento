package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.ExpedienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ExpedienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ExpedienteResponse;
import com.lucianodev.sistemaagendamento.entity.Expediente;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.factory.ExpedienteFactory;
import com.lucianodev.sistemaagendamento.factory.ProfissionalFactory;
import com.lucianodev.sistemaagendamento.mapper.ExpedienteMapper;
import com.lucianodev.sistemaagendamento.repository.ExpedienteRepository;
import com.lucianodev.sistemaagendamento.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpedienteServiceTest {

    private Expediente expediente;
    private ExpedienteResponse expedienteResponse;
    private ExpedienteCreateDto expedienteCreateDto;
    private ExpedienteUpdateDto updateDto;
    private Profissional profissional;

    @Mock
    private ExpedienteRepository expedienteRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;
    @Mock
    private ExpedienteMapper expedienteMapper;
    @InjectMocks
    private ExpedienteService expedienteService;

    @BeforeEach
    public void setUp() {
        expediente = ExpedienteFactory.createExpediente();
        expedienteResponse = ExpedienteFactory.expedienteResponse();
        expedienteCreateDto = ExpedienteFactory.createDto();
        updateDto = ExpedienteFactory.updateDto(LocalTime.of(8, 0), LocalTime.of(19, 0));
        profissional = ProfissionalFactory.createProfissional();
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarCriarExpedienteComProfissionalIdInexistente() {
        when(profissionalRepository.findById(expedienteCreateDto.profissionalId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> expedienteService.create(expedienteCreateDto));

        verify(profissionalRepository, times(1)).findById(expedienteCreateDto.profissionalId());
        verify(expedienteRepository, never()).save(any());
    }

    @Test
    public void deveLancarConflictException_QuandoJaExistirExpedienteNoMesmoDia() {
        when(profissionalRepository.findById(expedienteCreateDto.profissionalId())).thenReturn(Optional.of(profissional));
        when(expedienteRepository.existsByProfissionalAndDiaSemana(profissional, expedienteCreateDto.diaSemana())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> expedienteService.create(expedienteCreateDto));

        verify(profissionalRepository, times(1)).findById(expedienteCreateDto.profissionalId());
        verify(expedienteRepository, times(1)).existsByProfissionalAndDiaSemana(profissional, expedienteCreateDto.diaSemana());
        verify(expedienteRepository, never()).save(any());
    }

    @Test
    public void deveCriarExpedienteComSucesso() {
        when(profissionalRepository.findById(expedienteCreateDto.profissionalId())).thenReturn(Optional.of(profissional));
        when(expedienteRepository.existsByProfissionalAndDiaSemana(profissional, expedienteCreateDto.diaSemana())).thenReturn(false);
        when(expedienteMapper.toEntity(expedienteCreateDto, profissional)).thenReturn(expediente);
        when(expedienteRepository.save(expediente)).thenReturn(expediente);
        when(expedienteMapper.toResponse(expediente)).thenReturn(expedienteResponse);

        ExpedienteResponse result = expedienteService.create(expedienteCreateDto);
        assertNotNull(result);
        assertEquals(expedienteResponse.id(), result.id());
        assertEquals(expedienteResponse.diaSemana(), result.diaSemana());
        assertEquals(expedienteResponse.profissionalId(), result.profissionalId());

        verify(profissionalRepository, times(1)).findById(expedienteCreateDto.profissionalId());
        verify(expedienteRepository, times(1)).existsByProfissionalAndDiaSemana(profissional, expedienteCreateDto.diaSemana());
        verify(expedienteMapper, times(1)).toEntity(expedienteCreateDto, profissional);
        verify(expedienteRepository, times(1)).save(expediente);
        verify(expedienteMapper, times(1)).toResponse(expediente);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarAtualizarExpedienteComIdInexistente() {
        when(expedienteRepository.findById(expediente.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> expedienteService.update(expediente.getId(), updateDto));

        verify(expedienteRepository, times(1)).findById(expediente.getId());
        verify(expedienteRepository, never()).save(any());
    }

    @Test
    public void deveAtualizarExpedienteComSucesso() {
        when(expedienteRepository.findById(expediente.getId())).thenReturn(Optional.of(expediente));
        when(expedienteRepository.save(expediente)).thenReturn(expediente);
        when(expedienteMapper.toResponse(expediente)).thenReturn(expedienteResponse);

        expedienteService.update(expediente.getId(), updateDto);

        ArgumentCaptor<Expediente> captor = ArgumentCaptor.forClass(Expediente.class);

        verify(expedienteRepository, times(1)).findById(expediente.getId());
        verify(expedienteRepository, times(1)).save(captor.capture());
        verify(expedienteMapper, times(1)).toResponse(expediente);

        Expediente expedienteCaptor = captor.getValue();

        assertEquals(updateDto.horaInicio(), expedienteCaptor.getHoraInicio());
        assertEquals(updateDto.horaFim(), expedienteCaptor.getHoraFim());
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarExpedienteComIdInexistente() {
        when(expedienteRepository.findById(expediente.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> expedienteService.findById(expediente.getId()));

        verify(expedienteRepository, times(1)).findById(expediente.getId());
    }

    @Test
    public void deveBuscarExpedienteComSucesso() {
        when(expedienteRepository.findById(expediente.getId())).thenReturn(Optional.of(expediente));
        when(expedienteMapper.toResponse(expediente)).thenReturn(expedienteResponse);

        ExpedienteResponse result = expedienteService.findById(expediente.getId());
        assertNotNull(result);
        assertEquals(expedienteResponse.id(), result.id());

        verify(expedienteRepository, times(1)).findById(expediente.getId());
        verify(expedienteMapper, times(1)).toResponse(expediente);
    }

    @Test
    public void deveLancarResourceNotFoundException_QuandoTentarBuscarTodosExpedientesDoProssionalComIdInexistente() {
        when(profissionalRepository.existsById(profissional.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> expedienteService.findAllByProfissionalId(profissional.getId()));

        verify(profissionalRepository, times(1)).existsById(profissional.getId());
    }

    @Test
    public void deveRetornarListaDeExpedientesDoProfissionalComSucesso() {
        List<Expediente> listMock = List.of(expediente);
        when(profissionalRepository.existsById(profissional.getId())).thenReturn(true);
        when(expedienteRepository.findAllByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(profissional.getId())).thenReturn(listMock);
        when(expedienteMapper.toResponse(expediente)).thenReturn(expedienteResponse);

        List<ExpedienteResponse> list = expedienteService.findAllByProfissionalId(profissional.getId());
        assertNotNull(list);
        assertEquals(1, list.size());

        assertEquals(expedienteResponse.horaInicio(), list.get(0).horaInicio());
        assertEquals(expedienteResponse.diaSemana(), list.get(0).diaSemana());

        verify(profissionalRepository, times(1)).existsById(profissional.getId());
        verify(expedienteRepository, times(1)).findAllByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(profissional.getId());
        verify(expedienteMapper, times(1)).toResponse(expediente);
    }
}
