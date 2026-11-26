package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.AgendamentoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.AgendamentoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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

    @PatchMapping("/{id}/remarcar")
    public ResponseEntity<AgendamentoResponse> remarcar(@PathVariable UUID id,
                                                        @RequestBody @Valid AgendamentoUpdateDto dto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        return ResponseEntity.ok(agendamentoService.atualizarAgendamento(id, dto, email));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AgendamentoResponse> findAllByCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                      Pageable pageable) {
        String email = userDetails.getUsername();
        return agendamentoService.findAllByCliente(email, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgendamentoResponse findByIdAndCliente(@PathVariable UUID id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        return agendamentoService.findByIdAndCliente(id, email);
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        agendamentoService.cancelarAgendamentoComoCliente(id, email);
    }
}
