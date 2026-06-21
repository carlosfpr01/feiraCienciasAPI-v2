package br.com.escola.feiraciencias.projects.domain.model;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistroDiario {

    public static final int LIMITE_ARQUIVOS = 5;

    private Integer id;
    private String texto;
    private LocalDateTime dataCriacao;
    private Integer criadoPorId;
    private Integer projetoId;

    /**
     * Chaves de arquivos associados a este registro.
     * Armazena apenas as chaves (keys) do storage — nunca URLs.
     */
    private List<String> arquivoChaves = new ArrayList<>();

    public RegistroDiario(Integer id, String texto, LocalDateTime dataCriacao,
                          Integer criadoPorId, Integer projetoId) {
        this.id = id;
        this.texto = texto;
        this.dataCriacao = dataCriacao;
        this.criadoPorId = criadoPorId;
        this.projetoId = projetoId;
    }

    /**
     * Adiciona a chave de um arquivo ao registro.
     * Valida o limite de 5 arquivos por registro diário.
     */
    public void adicionarArquivoChave(String chave) {
        if (arquivoChaves.size() >= LIMITE_ARQUIVOS) {
            throw new BusinessRuleException(
                    "Limite de " + LIMITE_ARQUIVOS + " arquivos por registro diário atingido."
            );
        }
        arquivoChaves.add(chave);
    }

    /**
     * Remove a chave de um arquivo do registro.
     * Retorna true se a chave existia e foi removida.
     */
    public boolean removerArquivoChave(String chave) {
        return arquivoChaves.remove(chave);
    }

    public List<String> getArquivoChaves() {
        return Collections.unmodifiableList(arquivoChaves);
    }
}
