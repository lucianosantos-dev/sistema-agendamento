package com.lucianodev.sistemaagendamento.factory;



import com.lucianodev.sistemaagendamento.dto.request.ServicoCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ServicoUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ServicoResponse;
import com.lucianodev.sistemaagendamento.entity.Servico;

import java.math.BigDecimal;
import java.util.UUID;

public class ServicoFactory {

    private static final UUID ID_PADRAO = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public static Servico servicoEntity() {
        return new Servico(
                ID_PADRAO,
                "Corte e Barba",
                120,
                new BigDecimal("75.00")
        );
    }

    public static ServicoCreateDto servicoCreateDto() {
        Servico servico = servicoEntity();

        return new ServicoCreateDto(
                servico.getNome(),
                servico.getDuracao(),
                servico.getPreco()
        );
    }

    public static ServicoUpdateDto servicoUpdateDto(){
        Servico servico = servicoEntity();

        return new ServicoUpdateDto(
                servico.getNome(),
                servico.getDuracao(),
                servico.getPreco()
        );
    }

    public static ServicoResponse servicoResponse() {
        Servico servico = servicoEntity();

        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getDuracao(),
                servico.getPreco()
        );
    }
}
