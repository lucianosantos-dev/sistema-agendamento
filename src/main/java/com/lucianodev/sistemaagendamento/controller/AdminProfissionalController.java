package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.AssociarServicosRequestDto;
import com.lucianodev.sistemaagendamento.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/profissionais")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping("/{profissionalId}/servicos")
    public ResponseEntity<Map<String, String>> assocarServicos(@PathVariable UUID profissionalId,
                                                @RequestBody @Valid AssociarServicosRequestDto dto) {

        profissionalService.associarServicos(profissionalId, dto.servicosIds());

        Map<String, String> response = new HashMap<>();
        response.put("Resposta", "Serviços associados ao profissional escolhido com sucesso!");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{profissionalId}/servicos/{servicoId}")
    public ResponseEntity<Map<String, String>> removerServico(@PathVariable UUID profissionalId, @PathVariable UUID servicoId) {
        profissionalService.removerServico(profissionalId, servicoId);

        Map<String, String> response = new HashMap<>();

        response.put("Resposta", "Serviço removido com sucesso!");

        return ResponseEntity.ok(response);
    }
}
