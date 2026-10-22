package com.lucianodev.sistemaagendamento.controller;

import com.lucianodev.sistemaagendamento.dto.request.AlterarSenhaRequest;
import com.lucianodev.sistemaagendamento.dto.request.LoginDto;
import com.lucianodev.sistemaagendamento.dto.response.LoginResponse;
import com.lucianodev.sistemaagendamento.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/perfil/senha")
    public ResponseEntity<Map<String, String>> alterarSenha(@RequestBody @Valid
                                                            AlterarSenhaRequest request,
                                                            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        authService.alterarSenha(email, request);

        Map<String, String> response = Map.of(
                "Menssagem", "Ação realizada com sucesso."
        );
        return ResponseEntity.ok(response);
    }
}
