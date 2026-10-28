package com.lucianodev.sistemaagendamento.factory;

import com.lucianodev.sistemaagendamento.dto.request.ClienteCreateDto;
import com.lucianodev.sistemaagendamento.dto.request.ClienteUpdateDto;
import com.lucianodev.sistemaagendamento.dto.response.ClienteResponse;
import com.lucianodev.sistemaagendamento.entity.Cliente;
import com.lucianodev.sistemaagendamento.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClienteFactory {

    private static final UUID ID_PADRAO = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");


    public static Cliente createCliente(Usuario usuario) {
        return new Cliente(
                ID_PADRAO,
                usuario,
                "77981762234",
                "12345678901",
                LocalDate.of(2000, 1, 14),
                false,
                LocalDateTime.now()
        );
    }

    public static Cliente createCliente() {
        return createCliente(UsuarioFactory.usuarioEntity());
    }

    public static ClienteUpdateDto updateDto(String telefone, LocalDate dataNascimento) {
        return new ClienteUpdateDto(telefone, dataNascimento);
    }

    public static ClienteCreateDto createDto() {
        Cliente cliente = createCliente();

        return new ClienteCreateDto(
                cliente.getTelefone(),
                cliente.getCpf(),
                cliente.getDataNascimento()
        );
    }

    public static ClienteResponse clienteResponse() {
        Cliente cliente = createCliente();

        return new ClienteResponse(
                cliente.getId(),
                cliente.getUsuario().getId(),
                cliente.getTelefone(),
                "***.456.789-**",
                cliente.getDataNascimento(),
                cliente.getDataCadastro()
        );
    }
}
