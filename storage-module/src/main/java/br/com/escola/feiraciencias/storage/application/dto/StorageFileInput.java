package br.com.escola.feiraciencias.storage.application.dto;

/**
 * Dados de entrada para upload de arquivo.
 *
 * @param nomeOriginal nome original do arquivo (ex: "foto.png")
 * @param mimeType     tipo MIME do arquivo (ex: "image/png")
 * @param tamanho      tamanho em bytes
 * @param conteudo     bytes brutos do arquivo
 * @param prefixoChave prefixo opcional para organizar no bucket (ex: "events/123/capa")
 */
public record StorageFileInput(
        String nomeOriginal,
        String mimeType,
        long tamanho,
        byte[] conteudo,
        String prefixoChave
) {}
