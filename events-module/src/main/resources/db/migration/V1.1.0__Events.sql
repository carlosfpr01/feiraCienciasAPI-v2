-- MÓDULO: EVENTS - V1.1.0
CREATE TABLE eventos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    descricao TEXT,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    situacao VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    criado_por_id INTEGER NOT NULL REFERENCES usuarios(id)
);
