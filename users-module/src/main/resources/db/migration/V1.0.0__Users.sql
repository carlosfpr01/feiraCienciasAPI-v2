-- MÓDULO: USERS - V1.0.0
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL CHECK (tipo_usuario IN ('ADMIN', 'PROFESSOR', 'ALUNO')),
    data_cadastro TIMESTAMP NOT NULL,
    criado_por_id INTEGER REFERENCES usuarios(id)
);

CREATE TABLE alunos (
    id INTEGER PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    ano_escolar VARCHAR(30) NOT NULL
);

CREATE TABLE professores (
    id INTEGER PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    is_adm BOOLEAN NOT NULL DEFAULT FALSE,
    materia VARCHAR(120) NOT NULL
);

CREATE TABLE convites_registro (
    id           SERIAL PRIMARY KEY,
    token        VARCHAR(255) NOT NULL UNIQUE,
    nome         VARCHAR(150) NOT NULL,
    matricula    VARCHAR(50),
    tipo_usuario VARCHAR(50) NOT NULL CHECK (tipo_usuario IN ('ADMIN', 'PROFESSOR', 'ALUNO')),
    vinculo      VARCHAR(120) NOT NULL,
    criado_por_id INTEGER NOT NULL REFERENCES usuarios(id),
    data_criacao   TIMESTAMP NOT NULL,
    data_expiracao TIMESTAMP NOT NULL,
    status       VARCHAR(30) NOT NULL CHECK (status IN ('PENDENTE', 'USADO', 'EXPIRADO'))
);

CREATE INDEX idx_convites_token ON convites_registro(token);
CREATE INDEX idx_convites_status ON convites_registro(status);
