package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.ExpedienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ExpedienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ExpedienteResponse;
import com.lucianodev.sistemaagendamento.entity.Expediente;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.ExpedienteMapper;
import com.lucianodev.sistemaagendamento.repository.ExpedienteRepository;
import com.lucianodev.sistemaagendamento.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpedienteService {

    private final ExpedienteRepository repository;
    private final ProfissionalRepository profissionalRepository;
    private final ExpedienteMapper mapper;

    @Transactional
    public ExpedienteResponse create(ExpedienteCreateDto createDto) {
        Profissional profissional = profissionalRepository.findById(createDto.profissionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado."));

        if (repository.existsByProfissionalAndDiaSemana(profissional, createDto.diaSemana())) {
            throw new ConflictException("Já existe um expediente para esse profissional nesse dia.");
        }

        Expediente salvo = mapper.toEntity(createDto, profissional);
        return mapper.toResponse(repository.save(salvo));
    }

    @Transactional
    public ExpedienteResponse update(UUID id, ExpedienteUpdateDto updateDto) {
        Expediente expediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente não encontrado."));

        expediente.alterarHorario(updateDto.horaInicio(), updateDto.horaFim());

        return mapper.toResponse(repository.save(expediente));
    }

    @Transactional(readOnly = true)
    public ExpedienteResponse findById(UUID id) {
        Expediente expediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente não encontrado."));

        return mapper.toResponse(expediente);
    }

    @Transactional(readOnly = true)
    public Page<ExpedienteResponse> findAll(Pageable pageable) {
        Page<Expediente> page = repository.findAll(pageable);
        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ExpedienteResponse> findAllByProfissionalId(UUID profissionalId) {
        if (!profissionalRepository.existsById(profissionalId)) {
            throw new ResourceNotFoundException("Profissional não encontrado.");
        }

        List<Expediente> list = repository
                .findAllByProfissionalIdOrderByDiaSemanaAscHoraInicioAsc(profissionalId);

        return list.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
