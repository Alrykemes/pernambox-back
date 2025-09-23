-- Extensão para gerar UUIDs automaticamente
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE polo
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome      VARCHAR(150) NOT NULL,
    numero    VARCHAR(10)  NOT NULL,
    rua       VARCHAR(150) NOT NULL,
    bairro    VARCHAR(100) NOT NULL,
    municipio VARCHAR(100) NOT NULL UNIQUE,
    cep       VARCHAR(9)   NOT NULL
);

CREATE TABLE usuario
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome     VARCHAR(255) NOT NULL,
    cpf      VARCHAR(14)  NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(14),
    senha    VARCHAR(255) NOT NULL,
    polo_id  UUID          NOT NULL,
    CONSTRAINT fk_usuario_polo FOREIGN KEY (polo_id)
        REFERENCES polo (id) ON DELETE CASCADE
);