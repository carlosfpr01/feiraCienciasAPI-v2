-- =============================================================
--  Events Module - V1.1.1
--  Adiciona suporte a imagem de capa no evento.
-- =============================================================

ALTER TABLE eventos ADD COLUMN imagem_capa_chave VARCHAR(500);
