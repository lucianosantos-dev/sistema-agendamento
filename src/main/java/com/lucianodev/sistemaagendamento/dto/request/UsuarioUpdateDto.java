package com.lucianodev.sistemaagendamento.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDto (
        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome
){
}
