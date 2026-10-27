package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteCreateDto(

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "O CPF é obrigatório.")
        @CPF
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @PastOrPresent(message = "A data de nascimento não pode ser futura.")
        LocalDate dataNascimento
) {
}
