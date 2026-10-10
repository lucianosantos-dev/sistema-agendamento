CREATE TABLE usuarios(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    role VARCHAR(20) NOT NULL CHECK ( role IN ('ADMIN', 'CLIENTE')),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);