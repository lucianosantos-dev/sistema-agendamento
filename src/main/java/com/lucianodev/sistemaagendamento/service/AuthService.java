package com.lucianodev.sistemaagendamento.service;

import com.lucianodev.sistemaagendamento.configuration.JwtService;
import com.lucianodev.sistemaagendamento.dto.request.AlterarSenhaRequest;
import com.lucianodev.sistemaagendamento.dto.request.LoginDto;
import com.lucianodev.sistemaagendamento.dto.response.LoginResponse;
import com.lucianodev.sistemaagendamento.entity.Usuario;
import com.lucianodev.sistemaagendamento.exception.CredenciaisInvalidasException;
import com.lucianodev.sistemaagendamento.exception.ResourceNotFoundException;
import com.lucianodev.sistemaagendamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.CredentialException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public LoginResponse login(LoginDto loginDto) {
        Usuario usuario = repository.findByEmail(loginDto.email())
                .orElseThrow(() -> new CredenciaisInvalidasException(
                        "E-mail ou senha incorretos."));


        if (!passwordEncoder.matches(loginDto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
        }

        return new LoginResponse(jwtService.generateToken(usuario));
    }

    @Transactional
    public void alterarSenha(String email, AlterarSenhaRequest request) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com email informado: " + email));

        if (!passwordEncoder.matches(request.senhaAntiga(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual está incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        repository.save(usuario);
    }
}
