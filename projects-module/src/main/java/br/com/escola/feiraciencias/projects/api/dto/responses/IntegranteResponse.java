package br.com.escola.feiraciencias.projects.api.dto.responses;

import java.time.LocalDateTime;

public record IntegranteResponse(
    Integer id,
    Integer projetoId,
    Integer usuarioId,
    String tipoIntegrante,
    LocalDateTime dataVinculo
) {}
