package com.lucianodev.sistemaagendamento.factory;

import com.lucianodev.sistemaagendamento.dto.request.ExpedienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ExpedienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ExpedienteResponse;
import com.lucianodev.sistemaagendamento.entity.Expediente;
import com.lucianodev.sistemaagendamento.entity.Profissional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public class ExpedienteFactory {

    private static final UUID ID_PADRAO = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");


    public static Expediente createExpediente() {
        Profissional profissional = ProfissionalFactory.createProfissional();
        return new Expediente(
                ID_PADRAO,
                profissional,
                DayOfWeek.MONDAY,
                LocalTime.of(7, 30, 0),
                LocalTime.of(17, 30, 0)
        );
    }

    public static ExpedienteCreateDto createDto() {
        Expediente expediente = ExpedienteFactory.createExpediente();
        return new ExpedienteCreateDto(
                expediente.getProfissional().getId(),
                expediente.getDiaSemana(),
                expediente.getHoraInicio(),
                expediente.getHoraFim()
        );
    }

    public static ExpedienteResponse expedienteResponse() {
        Expediente expediente = ExpedienteFactory.createExpediente();

        return new ExpedienteResponse(
                expediente.getId(),
                expediente.getProfissional().getId(),
                expediente.getDiaSemana(),
                expediente.getHoraInicio(),
                expediente.getHoraFim()
        );
    }

    public static ExpedienteUpdateDto updateDto(LocalTime horaInicio, LocalTime horaFim) {
        return new ExpedienteUpdateDto(horaInicio, horaFim);
    }
}
