package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.UsuarioCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.UsuarioUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.UsuarioResponse;
import com.lucianodev.sistemaagendamento.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@RequestBody @Valid UsuarioCreateDto createDto) {
        UsuarioResponse response = service.create(createDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> update(
            @RequestBody @Valid UsuarioUpdateDto updateDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        return ResponseEntity.ok(service.update(email, updateDto));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @DeleteMapping("/perfil/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        service.excluir(email);
    }
}
