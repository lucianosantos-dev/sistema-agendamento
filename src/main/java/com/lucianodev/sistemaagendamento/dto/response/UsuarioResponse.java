package com.lucianodev.sistemaagendamento.dto.response;


import com.lucianodev.sistemaagendamento.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponse(
         UUID id,
         String nome,
         String email,
         Boolean ativo,
         LocalDateTime criadoEm,
         Role role
) {
}
