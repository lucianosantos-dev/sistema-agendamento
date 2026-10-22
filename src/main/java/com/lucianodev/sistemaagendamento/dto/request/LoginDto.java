package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "Email é obrigatório para login.")
        String email,
        @NotBlank(message = "Senha é obrigatória para login.")
        String senha
) {
}
