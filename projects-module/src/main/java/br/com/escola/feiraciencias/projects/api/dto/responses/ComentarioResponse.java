package br.com.escola.feiraciencias.projects.api.dto.responses;

import java.time.LocalDateTime;

public record ComentarioResponse(
    Integer id,
    String texto,
    Integer projetoId,
    Integer usuarioId,
    LocalDateTime dataCriacao
) {}
