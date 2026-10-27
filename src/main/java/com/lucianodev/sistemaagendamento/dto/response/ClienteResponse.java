package com.lucianodev.sistemaagendamento.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        UUID usuarioId,
        String telefone,
        String cpf,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro
) {
}
