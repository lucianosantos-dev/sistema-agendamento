package com.lucianodev.sistemaagendamento.enums;

public enum StatusAgendamento {
    PENDENTE,
    CONFIRMADO,
    EM_ATENDIMENTO,
    CANCELADO,
    CONCLUIDO,
    FALTA;

    public boolean bloqueiaHorario() {
        return this == PENDENTE || this == CONFIRMADO || this == EM_ATENDIMENTO;
    }
}
