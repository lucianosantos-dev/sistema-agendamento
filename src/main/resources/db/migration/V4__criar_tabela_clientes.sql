CREATE TABLE clientes(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    data_nascimento DATE NOT NULL,
    inativo_por_ausencia BOOLEAN NOT NULL DEFAULT FALSE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_clientes_cpf UNIQUE (cpf),
    CONSTRAINT uk_clientes_usuario_id UNIQUE (usuario_id),
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);