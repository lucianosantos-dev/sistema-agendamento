package com.lucianodev.sistemaagendamento.controller;


import com.lucianodev.sistemaagendamento.dto.request.ProfissionalCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ProfissionalUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ProfissionalResponse;
import com.lucianodev.sistemaagendamento.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService service;

    @PostMapping
    public ResponseEntity<ProfissionalResponse> save(@RequestBody @Valid ProfissionalCreateDto createDto) {
        ProfissionalResponse response = service.create(createDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponse> update(@PathVariable UUID id, @RequestBody @Valid ProfissionalUpdateDto updateDto) {
        ProfissionalResponse response = service.update(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
