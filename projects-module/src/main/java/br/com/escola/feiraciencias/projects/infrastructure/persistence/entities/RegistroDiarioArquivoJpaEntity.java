package br.com.escola.feiraciencias.projects.infrastructure.persistence.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registro_diario_arquivos")
@Getter
@Setter
@NoArgsConstructor
public class RegistroDiarioArquivoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Chave (key) do arquivo no storage.
     * Nunca armazena URL — a URL é resolvida sob demanda.
     */
    @Column(name = "arquivo_chave", nullable = false, length = 500)
    private String arquivoChave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_diario_id", nullable = false)
    private RegistroDiarioJpaEntity registroDiario;
}
