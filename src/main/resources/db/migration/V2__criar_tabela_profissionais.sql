CREATE TABLE profissionais(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    especialidade VARCHAR(150) NOT NULL,
    CONSTRAINT uk_profissionais_nome UNIQUE (nome)
);