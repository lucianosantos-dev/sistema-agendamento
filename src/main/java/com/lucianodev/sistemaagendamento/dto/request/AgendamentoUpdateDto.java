package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AgendamentoUpdateDto(
        @NotNull UUID profissionalId,
        @NotNull LocalDateTime novoInicio,
        @NotNull List<UUID> servicosIds
) {
}
