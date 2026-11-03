package com.lucianodev.sistemaagendamento.entity;

import com.lucianodev.sistemaagendamento.exception.HorarioInvalidoException;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "expedientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Expediente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;

    @Setter
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Setter
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    public void validarHorario(LocalTime inicio, LocalTime fim) {
        if (inicio == null || fim == null || !fim.isAfter(inicio)) {
            throw new HorarioInvalidoException("Hora fim deve ser posterior a hora inicio.");
        }
    }

    @PrePersist
    @PreUpdate
    public void validar() {
        validarHorario(this.horaInicio, this.horaFim);
    }

    public void alterarHorario(LocalTime novaHoraInicio, LocalTime novaHoraFim) {
        validarHorario(novaHoraInicio, novaHoraFim);
        this.horaInicio = novaHoraInicio;
        this.horaFim = novaHoraFim;
    }
}
