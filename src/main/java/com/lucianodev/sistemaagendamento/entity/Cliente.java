package com.lucianodev.sistemaagendamento.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    @Setter
    @Column(length = 15, nullable = false)
    private String telefone;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Setter
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "inativo_por_ausencia", nullable = false)
    private Boolean inativoPorAusencia = false;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        if (this.inativoPorAusencia == null) {
            this.inativoPorAusencia = false;
        }
    }

    public void inativarPorAusencia() {
        this.inativoPorAusencia = true;
    }
}
