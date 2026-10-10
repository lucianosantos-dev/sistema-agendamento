package com.lucianodev.sistemaagendamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 150, nullable = false, unique = true)
    private String nome;
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracao;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;
}
