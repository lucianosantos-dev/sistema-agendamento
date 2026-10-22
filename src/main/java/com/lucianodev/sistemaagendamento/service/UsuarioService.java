package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.dto.request.UsuarioCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.UsuarioUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.UsuarioResponse;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.enums.Role;
import com.lucianodev.sistemaagendamento.exception.ConflictException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.mapper.UsuarioMapper;
import com.lucianodev.sistemaagendamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UsuarioResponse create(UsuarioCreateDto createDto) {
        validaEmail(createDto.email());

        Usuario usuario = mapper.toEntity(createDto);

        usuario.setSenha(passwordEncoder.encode(createDto.senha()));
        usuario.setRole(Role.CLIENTE);

        Usuario salvo = repository.save(usuario);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public UsuarioResponse update(String email, UsuarioUpdateDto updateDto) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com e-mail informado: " + email));

        mapper.updateUsuario(updateDto, usuario);

        Usuario atualizado = repository.save(usuario);

        return mapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(UUID id) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com id informado: " + id));

        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        List<Usuario> list = repository.findAll();
        return list
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void ativar(UUID id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com id informado: " + id));

        user.reativar();
    }

    @Transactional
    public void desativar(UUID id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com id informado: " + id));

        user.desativar();
    }

    @Transactional
    public void excluir(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com e-mail informado: " + email));

        repository.delete(usuario);
    }

    private void validaEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new ConflictException("Já existe um usuário com esse email.");
        }
    }
}
