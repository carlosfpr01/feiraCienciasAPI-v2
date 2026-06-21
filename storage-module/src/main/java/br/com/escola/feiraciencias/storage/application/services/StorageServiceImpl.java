package br.com.escola.feiraciencias.storage.application.services;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.config.StorageProperties;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.storage.application.dto.StorageUploadResult;
import br.com.escola.feiraciencias.storage.infrastructure.client.S3StorageClient;
import br.com.escola.feiraciencias.storage.infrastructure.persistence.entities.ArquivoMetadadoJpaEntity;
import br.com.escola.feiraciencias.storage.infrastructure.persistence.repositories.ArquivoMetadadoPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementação do contrato {@link StorageService}.
 *
 * Orquestra: validação → upload no S3 → persistência de metadados no banco.
 * Módulo técnico — não possui nenhum conhecimento de domínio.
 */
@ApplicationScoped
public class StorageServiceImpl implements StorageService {

    @Inject
    StorageProperties properties;

    @Inject
    S3StorageClient s3Client;

    @Inject
    ArquivoMetadadoPanacheRepository metadadoRepository;

    @Override
    @Transactional
    public StorageUploadResult upload(StorageFileInput input, Integer usuarioId) {
        validarTipo(input.mimeType());
        validarTamanho(input.tamanho());

        String chave = gerarChave(input.prefixoChave(), input.nomeOriginal());

        s3Client.upload(chave, input.conteudo(), input.mimeType());

        ArquivoMetadadoJpaEntity metadado = new ArquivoMetadadoJpaEntity();
        metadado.setChave(chave);
        metadado.setNomeOriginal(input.nomeOriginal());
        metadado.setMimeType(input.mimeType());
        metadado.setTamanho(input.tamanho());
        metadado.setUploadPorId(usuarioId);
        metadado.setDataUpload(LocalDateTime.now());

        metadadoRepository.persist(metadado);

        return new StorageUploadResult(
                chave,
                input.nomeOriginal(),
                input.mimeType(),
                input.tamanho(),
                metadado.getDataUpload()
        );
    }

    @Override
    @Transactional
    public void delete(String chave) {
        if (chave == null || chave.isBlank()) {
            return;
        }

        s3Client.delete(chave);
        metadadoRepository.deleteByChave(chave);
    }

    @Override
    public String gerarUrl(String chave) {
        if (chave == null || chave.isBlank()) {
            return null;
        }
        return s3Client.gerarUrl(chave);
    }

    // ─── Validações ──────────────────────────────────────────────

    private void validarTipo(String mimeType) {
        if (mimeType == null || !properties.getTiposPermitidos().contains(mimeType)) {
            throw new BusinessRuleException(
                    "Tipo de arquivo não permitido: " + mimeType
                    + ". Tipos aceitos: " + String.join(", ", properties.getTiposPermitidos())
            );
        }
    }

    private void validarTamanho(long tamanho) {
        if (tamanho <= 0) {
            throw new BusinessRuleException("O arquivo está vazio.");
        }
        if (tamanho > properties.getTamanhoMaximoBytes()) {
            long maxMb = properties.getTamanhoMaximoBytes() / (1024 * 1024);
            throw new BusinessRuleException(
                    "Tamanho do arquivo excede o limite de " + maxMb + " MB."
            );
        }
    }

    // ─── Geração de chave ────────────────────────────────────────

    /**
     * Gera uma chave única no formato: prefixo/uuid-nomeoriginal
     * Exemplo: "events/42/capa/a1b2c3d4-foto.jpg"
     */
    private String gerarChave(String prefixo, String nomeOriginal) {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String nomeSeguro = nomeOriginal.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (prefixo != null && !prefixo.isBlank()) {
            return prefixo + "/" + uuid + "-" + nomeSeguro;
        }
        return uuid + "-" + nomeSeguro;
    }
}
