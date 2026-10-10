package com.lucianodev.sistemaagendamento.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoResponse(

        UUID id,
        String nome,
        Integer duracao,
        BigDecimal preco
) {
}
