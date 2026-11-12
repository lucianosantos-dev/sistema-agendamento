package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.AgendamentoCreateDto;
import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;


    @PostMapping
    public ResponseEntity<AgendamentoResponse> save(@RequestBody @Valid AgendamentoCreateDto dto,
                                                    @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        AgendamentoResponse response = agendamentoService.create(dto, email);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
