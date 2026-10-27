package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ClienteUpdateDto(

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @PastOrPresent(message = "A data de nascimento não pode ser futura.")
        LocalDate dataNascimento
) {
}
