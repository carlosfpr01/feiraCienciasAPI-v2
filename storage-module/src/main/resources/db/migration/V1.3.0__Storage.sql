-- =============================================================
--  Storage Module - V1.3.0
--  Metadados de arquivos armazenados no Cloudflare R2.
-- =============================================================

CREATE TABLE arquivos_metadados (
    chave          VARCHAR(500) PRIMARY KEY,
    nome_original  VARCHAR(255)  NOT NULL,
    mime_type      VARCHAR(100)  NOT NULL,
    tamanho        BIGINT        NOT NULL,
    upload_por_id  INTEGER       REFERENCES usuarios(id) ON DELETE SET NULL,
    data_upload    TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_arquivos_metadados_upload_por ON arquivos_metadados(upload_por_id);
