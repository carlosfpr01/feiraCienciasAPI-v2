-- MÓDULO: PROJECTS - V1.2.0
CREATE TABLE projetos (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    materiais TEXT,
    descricao TEXT,
    data_criacao DATE NOT NULL,
    data_apresentacao DATE,
    situacao VARCHAR(50) NOT NULL,
    area_de_conhecimento VARCHAR(100) NOT NULL,
    serie VARCHAR(50) NOT NULL,
    criado_por_id INTEGER NOT NULL REFERENCES usuarios(id),
    evento_id INTEGER NOT NULL REFERENCES eventos(id)
);

CREATE TABLE projeto_usuarios (
    id SERIAL PRIMARY KEY,
    projeto_id INTEGER NOT NULL REFERENCES projetos(id) ON DELETE CASCADE,
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    tipo_integrante VARCHAR(50) NOT NULL,
    data_vinculo TIMESTAMP NOT NULL
);

CREATE TABLE comentarios (
    id SERIAL PRIMARY KEY,
    texto TEXT NOT NULL,
    projeto_id INTEGER NOT NULL REFERENCES projetos(id) ON DELETE CASCADE,
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE registros_diarios (
    id SERIAL PRIMARY KEY,
    texto TEXT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    criado_por_id INTEGER NOT NULL REFERENCES usuarios(id),
    projeto_id INTEGER NOT NULL REFERENCES projetos(id) ON DELETE CASCADE
);

CREATE TABLE registro_diario_arquivos (
    id SERIAL PRIMARY KEY,
    arquivo_id VARCHAR(255) NOT NULL,       -- Chave gerada pelo storage-module (UUID)
    nome_original VARCHAR(255) NOT NULL,    -- Nome original do arquivo (ex: "foto.png")
    registro_diario_id INTEGER NOT NULL REFERENCES registros_diarios(id) ON DELETE CASCADE
);
