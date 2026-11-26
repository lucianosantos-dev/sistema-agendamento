package com.lucianodev.sistemaagendamento.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "agendamentos_servicos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgendamentoServico {

    @EmbeddedId
    private AgendamentoServicoId id = new AgendamentoServicoId();

    @MapsId("agendamentoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @MapsId("servicoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    AgendamentoServico(Agendamento agendamento, Servico servico) {
        this.agendamento = agendamento;
        this.servico = servico;
        this.duracaoMinutos = servico.getDuracao();
        this.preco = servico.getPreco();
    }
}
