package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ServicoUpdateDto(

        @NotBlank(message = "O nome do serviço é obrigatório.")
        String nome,

        @NotNull(message = "A duraçao em minutos é obrigatória.")
        @Positive(message = "A duração deve ser maior que zero.")
        @Max(value = 720, message = "A duração não pode exceder 12 horas (720 minutos).")
        Integer duracao,

        @Positive(message = "O preço deve ser maior que 0.")
        @NotNull(message = "O preço é obrigatório.")
        BigDecimal preco
) {
}
