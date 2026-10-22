package com.lucianodev.sistemaagendamento.entity;

import com.lucianodev.sistemaagendamento.enums.Role;
import com.lucianodev.sistemaagendamento.exception.OperacaoInvalidaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(length = 100, nullable = false)
    private String nome;

    @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "senha_hash", length = 255, nullable = false)
    private String senha;

    @Setter
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean ehAdmin() {
        return Role.ADMIN.equals(this.role);
    }

    public void desativar() {
        if (ehAdmin()) {
            throw new OperacaoInvalidaException("Erro! Um Administrador do sistema não pode ser desativado.");
        }
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }
}
