package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.response.AgendamentoResponse;
import com.lucianodev.sistemaagendamento.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/agendamentos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAgendamentoController {

    private final AgendamentoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AgendamentoResponse> findAllByAdmin(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHoraInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHoraFim,
            Pageable pageable) {

        if (dataHoraInicio != null && dataHoraFim != null) {
            return service.findByDataHoraInicioBetween(dataHoraInicio, dataHoraFim, pageable);
        }
        return service.findAllByAdmin(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgendamentoResponse findByIdAdmin(@PathVariable UUID id) {
        return (service.findByIdAdmin(id));
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable UUID id) {
        service.cancelarAgendamentoComoAdmin(id);
    }

    @PatchMapping("/{id}/registrar-chegada")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registrarChegada(@PathVariable UUID id) {
        service.registrarChegada(id);
    }

    @PatchMapping("/{id}/registrar-falta")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registrarFalta(@PathVariable UUID id) {
        service.registrarFalta(id);
    }

    @PatchMapping("/{id}/concluir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluir(@PathVariable UUID id) {
        service.concluir(id);
    }
}
