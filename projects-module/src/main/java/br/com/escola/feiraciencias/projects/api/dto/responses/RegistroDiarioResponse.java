package br.com.escola.feiraciencias.projects.api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

public record RegistroDiarioResponse(
    Integer id,
    String texto,
    LocalDateTime dataCriacao,
    Integer criadoPorId,
    Integer projetoId,
    List<RegistroDiarioArquivoResponse> arquivos
) {}
