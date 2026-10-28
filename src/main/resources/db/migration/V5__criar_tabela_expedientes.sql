CREATE TABLE expedientes(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profissional_id UUID NOT NULL,
    dia_semana SMALLINT NOT NULL CHECK ( dia_semana >= 1 AND dia_semana <= 7),
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,

    CONSTRAINT chk_horario_valido CHECK (hora_fim > hora_inicio),
    CONSTRAINT uk_expediente_profissional_dia UNIQUE (profissional_id, dia_semana),
    FOREIGN KEY (profissional_id) REFERENCES profissionais (id) ON DELETE RESTRICT
);