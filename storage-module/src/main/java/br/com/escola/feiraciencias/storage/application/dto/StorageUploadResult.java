package br.com.escola.feiraciencias.storage.application.dto;

import java.time.LocalDateTime;

/**
 * Resultado de um upload bem-sucedido.
 *
 * @param chave        chave (key) do arquivo no storage — o que é persistido no banco dos módulos consumidores
 * @param nomeOriginal nome original do arquivo
 * @param mimeType     tipo MIME
 * @param tamanho      tamanho em bytes
 * @param dataUpload   data/hora do upload
 */
public record StorageUploadResult(
        String chave,
        String nomeOriginal,
        String mimeType,
        long tamanho,
        LocalDateTime dataUpload
) {}
