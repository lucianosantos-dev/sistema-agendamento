package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.ExpedienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ExpedienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ExpedienteResponse;
import com.lucianodev.sistemaagendamento.service.ExpedienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/expedientes")
@RequiredArgsConstructor
public class AdminExpedienteController {

    private final ExpedienteService expedienteService;

    @PostMapping
    public ResponseEntity<ExpedienteResponse> save(@RequestBody @Valid ExpedienteCreateDto createDto) {
        ExpedienteResponse response = expedienteService.create(createDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpedienteResponse> update(@PathVariable UUID id, @RequestBody @Valid ExpedienteUpdateDto updateDto) {
        return ResponseEntity.ok(expedienteService.update(id, updateDto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpedienteResponse findById(@PathVariable UUID id) {
        return expedienteService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ExpedienteResponse> page(@PageableDefault(
            page = 0,
            size = 5,
            sort = "diaSemana",
            direction = Sort.Direction.ASC) Pageable pageable) {

        return expedienteService.findAll(pageable);
    }

    @GetMapping("/profissional/{profissionalId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ExpedienteResponse> findAllByProfissional(@PathVariable UUID profissionalId) {
        return expedienteService.findAllByProfissionalId(profissionalId);
    }
}
