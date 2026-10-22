package com.lucianodev.sistemaagendamento.dto.request;

import com.lucianodev.sistemaagendamento.validation.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDto(

        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @NotBlank(message = "O email é obrigatório.")
        @Email
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
        @SenhaForte
        String senha
) {
}
