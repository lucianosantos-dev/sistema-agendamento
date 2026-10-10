package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfissionalCreateDto(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,
        @NotBlank(message = "A especialidade é obrigatória.")
        String especialidade
) {
}
