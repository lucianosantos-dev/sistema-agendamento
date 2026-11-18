DROP INDEX ux_agendamento_ativo_por_cliente;

ALTER TABLE agendamentos
    ADD CONSTRAINT ex_agendamento_cliente_sem_sobreposicao
    EXCLUDE USING gist (
      cliente_id WITH =,
      (tsrange(data_hora_inicio, data_hora_fim, '[)')) WITH &&
  ) WHERE (status IN ('PENDENTE','CONFIRMADO','EM_ATENDIMENTO'));