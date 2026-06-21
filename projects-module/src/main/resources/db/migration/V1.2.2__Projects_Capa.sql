-- =============================================================
--  Projects Module - V1.2.2
--  Adiciona suporte a imagem de capa no projeto.
-- =============================================================

ALTER TABLE projetos ADD COLUMN imagem_capa_chave VARCHAR(500);
