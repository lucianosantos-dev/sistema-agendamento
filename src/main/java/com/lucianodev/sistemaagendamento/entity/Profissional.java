package com.lucianodev.sistemaagendamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "profissionais")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String nome;

    @Setter
    @Column(length = 150, nullable = false)
    private String especialidade;

    @ManyToMany
    @JoinTable(name = "profissionais_servicos",
            joinColumns = @JoinColumn(name = "profissional_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "servico_id", nullable = false))
    private Set<Servico> servicos = new HashSet<>();
}
