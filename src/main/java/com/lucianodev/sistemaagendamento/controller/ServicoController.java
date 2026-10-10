package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.ServicoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ServicoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ServicoResponse;
import com.lucianodev.sistemaagendamento.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService service;

    @PostMapping
    public ResponseEntity<ServicoResponse> save(@Valid @RequestBody ServicoCreateDto createDto) {
        ServicoResponse response = service.create(createDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> update(@PathVariable UUID id, @Valid @RequestBody ServicoUpdateDto update){
        ServicoResponse response = service.update(id, update);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> findAll() {
        List<ServicoResponse> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> findById(@PathVariable UUID id) {
        ServicoResponse response = service.findById(id);
        return ResponseEntity.ok().body(response);
    }
}
