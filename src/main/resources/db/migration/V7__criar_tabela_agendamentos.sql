CREATE
EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE profissionais_servicos
(
    profissional_id UUID NOT NULL,
    servico_id      UUID NOT NULL,

    CONSTRAINT pk_profissionais_servicos PRIMARY KEY (profissional_id, servico_id),
    CONSTRAINT fk_profissionais_servicos_profissional
        FOREIGN KEY (profissional_id) REFERENCES profissionais (id) ON DELETE RESTRICT,
    CONSTRAINT fk_profissionais_servicos_servico
        FOREIGN KEY (servico_id) REFERENCES servicos (id) ON DELETE RESTRICT
);

CREATE INDEX ix_profissionais_servicos_servico ON profissionais_servicos (servico_id);

CREATE TABLE agendamentos
(
    id               UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    cliente_id       UUID        NOT NULL,
    profissional_id  UUID        NOT NULL,
    data_hora_inicio TIMESTAMP   NOT NULL,
    data_hora_fim    TIMESTAMP   NOT NULL,
    status           VARCHAR(20) NOT NULL,
    qtd_remarcacoes  INTEGER     NOT NULL DEFAULT 0,
    chegada_em       TIMESTAMP,
    cancelado_em     TIMESTAMP,
    criado_em        TIMESTAMP   NOT NULL,

    CONSTRAINT fk_agendamentos_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE RESTRICT,
    CONSTRAINT fk_agendamentos_profissional
        FOREIGN KEY (profissional_id) REFERENCES profissionais (id) ON DELETE RESTRICT,
    CONSTRAINT ck_agendamento_horario CHECK (data_hora_fim > data_hora_inicio),
    CONSTRAINT ck_agendamento_status CHECK (status IN
                                            ('PENDENTE', 'CONFIRMADO', 'EM_ATENDIMENTO', 'CANCELADO', 'CONCLUIDO',
                                             'FALTA')),
    CONSTRAINT ck_agendamento_remarcacoes CHECK (qtd_remarcacoes >= 0),
    CONSTRAINT       ex_agendamento_sem_sobreposicao EXCLUDE USING gist (
        profissional_id WITH =,
        (tsrange(data_hora_inicio, data_hora_fim, '[)')) WITH &&
    ) WHERE (status IN ('PENDENTE', 'CONFIRMADO', 'EM_ATENDIMENTO'))
);

CREATE UNIQUE INDEX ux_agendamento_ativo_por_cliente
    ON agendamentos (cliente_id) WHERE status IN ('PENDENTE', 'CONFIRMADO', 'EM_ATENDIMENTO');

CREATE INDEX ix_agendamento_cliente_inicio ON agendamentos (cliente_id, data_hora_inicio);
CREATE INDEX ix_agendamento_status_inicio ON agendamentos (status, data_hora_inicio);

CREATE TABLE agendamentos_servicos
(
    agendamento_id  UUID           NOT NULL,
    servico_id      UUID           NOT NULL,
    duracao_minutos INTEGER        NOT NULL,
    preco           NUMERIC(10, 2) NOT NULL,

    CONSTRAINT pk_agendamentos_servicos PRIMARY KEY (agendamento_id, servico_id),
    CONSTRAINT fk_agendamentos_servicos_agendamento
        FOREIGN KEY (agendamento_id) REFERENCES agendamentos (id) ON DELETE RESTRICT,
    CONSTRAINT fk_agendamentos_servicos_servico
        FOREIGN KEY (servico_id) REFERENCES servicos (id) ON DELETE RESTRICT,
    CONSTRAINT ck_agendamentos_servicos_duracao CHECK (duracao_minutos > 0),
    CONSTRAINT ck_agendamentos_servicos_preco CHECK (preco >= 0)
);

CREATE INDEX ix_agendamentos_servicos_servico ON agendamentos_servicos (servico_id);