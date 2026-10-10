package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.ServicoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ServicoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ServicoResponse;
import com.lucianodev.sistemaagendamento.entity.Servico;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.ServicoMapper;
import com.lucianodev.sistemaagendamento.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper servicoMapper;

    @Transactional
    public ServicoResponse create(ServicoCreateDto create) {
        if (repository.existsByNome(create.nome())) {
            throw new ConflictException("Existe um serviço com esse nome.");
        }

        Servico entity = servicoMapper.toEntity(create);
        return servicoMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ServicoResponse update(UUID id, ServicoUpdateDto update) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id informado: " + id));

        servicoMapper.updateEntity(update, servico);

        Servico atualizado = repository.save(servico);
        return servicoMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> findAll() {
        List<Servico> list = repository.findAll();
        return list
                .stream()
                .map(servicoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicoResponse findById(UUID id) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id informado: " + id));
        return servicoMapper.toResponse(servico);
    }
}
