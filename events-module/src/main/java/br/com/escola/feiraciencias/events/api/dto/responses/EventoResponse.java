package br.com.escola.feiraciencias.events.api.dto.responses;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EventoResponse(
    Integer id,
    String nome,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim,
    SituacaoEvento situacao,
    LocalDateTime dataCriacao,
    Integer criadoPorId,
    String imagemCapaUrl
) {}
