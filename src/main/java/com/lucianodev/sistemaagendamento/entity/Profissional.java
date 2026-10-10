package com.lucianodev.sistemaagendamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "profissionais")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 150, nullable = false, unique = true)
    private String nome;

    @Column(length = 150, nullable = false)
    private String especialidade;
}
