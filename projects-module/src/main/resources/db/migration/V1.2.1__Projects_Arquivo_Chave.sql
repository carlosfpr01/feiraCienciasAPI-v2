-- =============================================================
--  Projects Module - V1.2.1
--  Altera tabela de arquivos de registros diários para armazenar
--  apenas a chave do storage (sem UUID local e sem nome original)
-- =============================================================

-- Remover as colunas antigas
ALTER TABLE registro_diario_arquivos DROP COLUMN arquivo_id;
ALTER TABLE registro_diario_arquivos DROP COLUMN nome_original;

-- Adicionar a nova coluna que armazena a chave gerada pelo storage-module
ALTER TABLE registro_diario_arquivos ADD COLUMN arquivo_chave VARCHAR(500) NOT NULL;
