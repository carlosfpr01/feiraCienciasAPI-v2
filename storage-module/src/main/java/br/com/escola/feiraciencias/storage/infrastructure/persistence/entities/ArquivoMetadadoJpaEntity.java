package br.com.escola.feiraciencias.storage.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Metadados de cada arquivo armazenado.
 *
 * A chave (key) do objeto no storage é a PK — não armazenamos URL.
 * A referência ao usuário usa ON DELETE SET NULL: se o usuário for excluído,
 * o arquivo permanece, mas perde a rastreabilidade de quem fez o upload.
 */
@Entity
@Table(name = "arquivos_metadados")
@Getter
@Setter
@NoArgsConstructor
public class ArquivoMetadadoJpaEntity {

    @Id
    @Column(nullable = false, length = 500)
    private String chave;

    @Column(name = "nome_original", nullable = false, length = 255)
    private String nomeOriginal;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(nullable = false)
    private Long tamanho;

    @Column(name = "upload_por_id")
    private Integer uploadPorId;

    @Column(name = "data_upload", nullable = false)
    private LocalDateTime dataUpload;
}
