package com.lucianodev.sistemaagendamento.dto.response;

import java.util.UUID;

public record ProfissionalResponse(
        UUID id,
        String nome,
        String especialidade
) {
}
