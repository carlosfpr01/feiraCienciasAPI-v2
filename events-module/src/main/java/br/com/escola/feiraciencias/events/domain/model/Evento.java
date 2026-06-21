package br.com.escola.feiraciencias.events.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    private Integer id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private SituacaoEvento situacao;
    private LocalDateTime dataCriacao;
    private Integer criadoPorId;

    /**
     * Chave do arquivo de capa no storage.
     * Opcional — o evento pode não ter capa.
     * Nunca armazena URL, apenas a chave (key).
     */
    private String imagemCapaChave;
}
