package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.ClienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ClienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clientes")
@PreAuthorize("hasRole('CLIENTE')")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;


    @PostMapping("/perfil")
    public ResponseEntity<ClienteResponse> save(
            @RequestBody @Valid ClienteCreateDto request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        ClienteResponse response = service.create(request, email);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/perfil")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponse update(
            @RequestBody @Valid ClienteUpdateDto updateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        return service.update(updateDto, email);
    }

    @GetMapping("/perfil")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponse findByEmail(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return service.findByEmail(email);
    }
}
