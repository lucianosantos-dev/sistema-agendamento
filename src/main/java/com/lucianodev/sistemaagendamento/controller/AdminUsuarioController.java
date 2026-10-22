package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarUsuario(@PathVariable UUID id){
        usuarioService.ativar(id);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarUsuario(@PathVariable UUID id){
        usuarioService.desativar(id);
    }
}
