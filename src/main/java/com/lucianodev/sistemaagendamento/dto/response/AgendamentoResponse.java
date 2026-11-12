package com.lucianodev.sistemaagendamento.dto.response;

import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoResponse(
        UUID id,
        String nomeProfissional,
        LocalDateTime inicio,
        LocalDateTime fim,
        StatusAgendamento status
) {
}
