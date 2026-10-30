package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ExpedienteUpdateDto(

        @NotNull(message = "A hora de inicio é obrigatória.")
        LocalTime horaInicio,
        @NotNull(message = "A hora do fim é obrigatória.")
        LocalTime horaFim
) {
}
