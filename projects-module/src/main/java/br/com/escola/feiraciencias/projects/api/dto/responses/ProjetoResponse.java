package br.com.escola.feiraciencias.projects.api.dto.responses;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import java.time.LocalDate;

public record ProjetoResponse(
    Integer id,
    String titulo,
    String descricao,
    String materiais,
    LocalDate dataCriacao,
    LocalDate dataApresentacao,
    SituacaoProjeto situacao,
    String areaDeConhecimento,
    String serie,
    Integer criadoPorId,
    Integer eventoId,
    String imagemCapaUrl
) {}
