package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record AssociarServicosRequestDto(
        @NotEmpty(message = "Informe ao menos um serviço.")
        List<UUID> servicosIds
) {
}
