package br.com.escola.feiraciencias.projects.domain.model;

import java.time.LocalDate;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {
    private Integer id;
    private String titulo;
    private String descricao;
    private String materiais;
    private LocalDate dataCriacao;
    private LocalDate dataApresentacao;
    private SituacaoProjeto situacao;
    private String areaDeConhecimento;
    private String serie;
    private Integer criadoPorId;
    private Integer eventoId;
    private String imagemCapaChave;
}
