package com.lucianodev.sistemaagendamento.entity;

import com.lucianodev.sistemaagendamento.enums.StatusAgendamento;
import com.lucianodev.sistemaagendamento.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "agendamentos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(name = "data_hora_inicio", nullable = false, updatable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false, updatable = false)
    private LocalDateTime dataHoraFim;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    @Column(name = "qtd_remarcacoes", nullable = false)
    private Integer qtdRemarcacoes;

    @Column(name = "chegada_em")
    private LocalDateTime chegadaEm;

    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;


    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.PERSIST)
    private List<AgendamentoServico> servicos = new ArrayList<>();

    public static Agendamento criar(Cliente cliente, Profissional profissional,
                                    LocalDateTime inicio, List<Servico> servicosEscolhidos,
                                    LocalDateTime agora) {
        Agendamento agendamento = new Agendamento();
        agendamento.cliente = cliente;
        agendamento.profissional = profissional;
        agendamento.dataHoraInicio = inicio;
        agendamento.status = StatusAgendamento.CONFIRMADO;
        agendamento.criadoEm = agora;
        agendamento.qtdRemarcacoes = 0;
        servicosEscolhidos.forEach(servico -> agendamento.servicos.add(new AgendamentoServico(agendamento, servico)));
        agendamento.dataHoraFim = inicio.plusMinutes(agendamento.calcularDuracaoTotalMinutos());
        return agendamento;
    }

    public void cancelar(LocalDateTime agora) {
        if (status != StatusAgendamento.PENDENTE && status != StatusAgendamento.CONFIRMADO) {
            throw new BusinessException("Não é possível cancelar um agendamento " + status + ".");
        }
        this.status = StatusAgendamento.CANCELADO;
        this.canceladoEm = agora;
    }

    public void registrarChegada(LocalDateTime agora) {
        if (status == StatusAgendamento.CONFIRMADO) {
            this.status = StatusAgendamento.EM_ATENDIMENTO;
            this.chegadaEm = agora;
        } else {
            throw new BusinessException("Não é possível registrar chegada para um agendamento " + status + ".");
        }
    }

    public void registrarFalta() {
        if (status == StatusAgendamento.CONFIRMADO) {
            this.status = StatusAgendamento.FALTA;
        } else {
            throw new BusinessException("Não foi possível registrar falta para esse agendamento " + status + ".");
        }
    }

    public void concluir() {
        if (status == StatusAgendamento.EM_ATENDIMENTO) {
            this.status = StatusAgendamento.CONCLUIDO;
        } else {
            throw new BusinessException("Não foi possível concluir esse agendamento " + status + ".");
        }
    }

    public int calcularDuracaoTotalMinutos() {
        return servicos
                .stream()
                .mapToInt(AgendamentoServico::getDuracaoMinutos)
                .sum();
    }

    public BigDecimal calcularValorTotal() {
        return servicos
                .stream()
                .map(AgendamentoServico::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<StatusAgendamento> bloqueantes() {
        return Arrays.stream(StatusAgendamento.values())
                .filter(StatusAgendamento::bloqueiaHorario)
                .toList();
    }
}
