package com.lucianodev.sistemaagendamento.factory;

import com.lucianodev.sistemaagendamento.dto.request.UsuarioCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.UsuarioUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.UsuarioResponse;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class UsuarioFactory {

    private static final UUID ID_PADRAO = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public static Usuario usuarioEntity() {
        return new Usuario(
                ID_PADRAO,
                "Usuario Entity testes",
                "usuariotest@email.com",
                "$2a$10$XURPShQNCsLjp1ESc2laoO46CGkVvyBJndLn659f51CEU3J29hO76",
                true,
                LocalDateTime.now(),
                Role.CLIENTE
        );
    }

    public static UsuarioUpdateDto usuarioUpdateDto(String nomeAtualizado) {
        return new UsuarioUpdateDto(nomeAtualizado);
    }

    public static UsuarioCreateDto usuarioCreateDto() {
        Usuario usuario = usuarioEntity();

        return new UsuarioCreateDto(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        );
    }

    public static UsuarioResponse usuarioResponse() {
        Usuario usuario = usuarioEntity();

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getCriadoEm(),
                usuario.getRole()
        );
    }
}
