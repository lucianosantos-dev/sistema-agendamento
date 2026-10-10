package com.lucianodev.sistemaagendamento.service;


import com.lucianodev.sistemaagendamento.dto.request.ProfissionalCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ProfissionalUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ProfissionalResponse;
import com.lucianodev.sistemaagendamento.entity.Profissional;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.ProfissionalMapper;
import com.lucianodev.sistemaagendamento.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalMapper profissionalMapper;


    @Transactional
    public ProfissionalResponse create(ProfissionalCreateDto createDto) {
        if (profissionalRepository.existsByNome(createDto.nome())) {
            throw new ConflictException("Já existe um profissional com esse nome.");
        }

        Profissional entity = profissionalMapper.toEntity(createDto);
        return profissionalMapper.toResponse(profissionalRepository.save(entity));
    }

    @Transactional
    public ProfissionalResponse update(UUID id, ProfissionalUpdateDto updateDto) {
        Profissional entity = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado."));

        profissionalMapper.update(updateDto, entity);

        Profissional atualizado = profissionalRepository.save(entity);
        return profissionalMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public ProfissionalResponse findById(UUID id) {
        Profissional entity = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado."));

        return profissionalMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponse> findAll() {
        List<Profissional> list = profissionalRepository.findAll();
        return list
                .stream()
                .map(profissionalMapper::toResponse)
                .toList();
    }
}
