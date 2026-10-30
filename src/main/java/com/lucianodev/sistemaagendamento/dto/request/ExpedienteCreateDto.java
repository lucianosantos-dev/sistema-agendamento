package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record ExpedienteCreateDto(

        @NotNull(message = "O profissional é obrigatório.")
        UUID profissionalId,
        @NotNull(message = "O dia da semana é obrigatório.")
        DayOfWeek diaSemana,
        @NotNull(message = "A hora de inicio é obrigatória.")
        LocalTime horaInicio,
        @NotNull(message = "A hora do fim é obrigatória.")
        LocalTime horaFim
) {
}
