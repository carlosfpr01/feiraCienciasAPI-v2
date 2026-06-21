package br.com.escola.feiraciencias.storage.application.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Optional;

/**
 * Propriedades configuráveis do módulo de armazenamento.
 * Limites de tipo e tamanho de arquivo são definidos via application.properties.
 */
@ApplicationScoped
public class StorageProperties {

    /**
     * Tipos MIME permitidos para upload.
     * Ex: image/png,image/jpeg,application/pdf
     */
    @ConfigProperty(name = "storage.upload.tipos-permitidos",
            defaultValue = "image/png,image/jpeg,image/gif,image/webp,application/pdf,video/mp4")
    List<String> tiposPermitidos;

    /**
     * Tamanho máximo por arquivo em bytes.
     * Padrão: 10 MB (10485760 bytes)
     */
    @ConfigProperty(name = "storage.upload.tamanho-maximo-bytes",
            defaultValue = "10485760")
    long tamanhoMaximoBytes;

    public List<String> getTiposPermitidos() {
        return tiposPermitidos;
    }

    public long getTamanhoMaximoBytes() {
        return tamanhoMaximoBytes;
    }
}
