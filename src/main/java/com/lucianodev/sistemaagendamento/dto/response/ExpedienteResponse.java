package com.lucianodev.sistemaagendamento.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record ExpedienteResponse(
        UUID id,
        UUID profissionalId,
        DayOfWeek diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
) {
}
