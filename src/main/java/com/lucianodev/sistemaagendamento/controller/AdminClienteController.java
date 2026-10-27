package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/clientes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminClienteController {

    private final ClienteService clienteService;


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponse findById(@PathVariable UUID id) {
        return clienteService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ClienteResponse> findAll(@PageableDefault(
            size = 5,
            page = 0,
            sort = "dataCadastro",
            direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return clienteService.findAll(pageable);
    }
}
