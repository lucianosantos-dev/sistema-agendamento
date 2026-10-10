CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE servicos(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL UNIQUE,
    duracao_minutos INTEGER NOT NULL,
    preco NUMERIC(10,2) NOT NULL check ( preco > 0)
);