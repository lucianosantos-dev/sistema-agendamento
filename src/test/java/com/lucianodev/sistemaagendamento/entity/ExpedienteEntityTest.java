package com.lucianodev.sistemaagendamento.entity;

import com.lucianodev.sistemaagendamento.exception.HorarioInvalidoException;
import com.lucianodev.sistemaagendamento.factory.ExpedienteFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpedienteEntityTest {

    @Test
    public void deveAtualizarHorario_QuandoValidos() {
        Expediente expediente = ExpedienteFactory.createExpediente();

        expediente.alterarHorario(LocalTime.of(9, 0, 0),
                LocalTime.of(22, 30, 0));

        assertThat(LocalTime.of(9, 0, 0)).isEqualTo(expediente.getHoraInicio());
        assertThat(LocalTime.of(22, 30, 0)).isEqualTo(expediente.getHoraFim());
    }

    @Test
    public void deveLancarHorarioInvalidoException_QuandoTentarAlterarHorario() {
        Expediente expediente = ExpedienteFactory.createExpediente();
        LocalTime inicioOriginal = expediente.getHoraInicio();

        assertThrows(HorarioInvalidoException.class,
                () -> expediente.alterarHorario(LocalTime.of(22, 30, 0), LocalTime.of(9, 0, 0)));

        assertThat(expediente.getHoraInicio()).isEqualTo(inicioOriginal);
    }

    @Test
    public void deveLancarHorarioInvalidoException_QuandoHorariosForemNulos() {
        Expediente expediente = ExpedienteFactory.createExpediente();

        assertThrows(HorarioInvalidoException.class,
                () -> expediente.alterarHorario(null, LocalTime.of(22, 30, 0)));


        assertThrows(HorarioInvalidoException.class,
                () -> expediente.alterarHorario(LocalTime.of(7, 30, 0), null));
    }
}
