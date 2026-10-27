package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.ClienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ClienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.entity.Cliente;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.enums.Role;
import com.lucianodev.sistemaagendamento.exception.AcessoNegadoException;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.ClienteMapper;
import com.lucianodev.sistemaagendamento.repository.ClienteRepository;
import com.lucianodev.sistemaagendamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper mapper;


    @Transactional
    public ClienteResponse create(ClienteCreateDto createRequest, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Usuário não encontrado."));

        if (!usuario.getRole().equals(Role.CLIENTE)) {
            throw new AcessoNegadoException("Apenas usuários com role de CLIENTE pode ter perfil de cliente.");
        }
        if (repository.existsByUsuario(usuario)) {
            throw new ConflictException("Esse usuário já possui perfil de cliente.");
        }

        Cliente cliente = mapper.toEntity(createRequest, usuario);
        return mapper.toResponse(repository.save(cliente));
    }

    @Transactional
    public ClienteResponse update(ClienteUpdateDto updateRequest, String email) {
        Cliente cliente = repository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));


        if (!cliente.getUsuario().getEmail().equals(email)) {
            throw new AcessoNegadoException("Você não tem permissão para atualizar esse perfil.");
        }

        mapper.updateEntity(updateRequest, cliente);

        Cliente atualizado = repository.save(cliente);
        return mapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public ClienteResponse findByEmail(String email) {
        Cliente cliente = repository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de Cliente não encontrado."));
        return mapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse findById(UUID id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return mapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> findAll(Pageable pageable) {
        Page<Cliente> page = repository.findAll(pageable);
        return page.map(mapper::toResponse);
    }
}
