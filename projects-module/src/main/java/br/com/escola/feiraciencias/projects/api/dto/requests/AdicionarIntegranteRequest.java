package br.com.escola.feiraciencias.projects.api.dto.requests;

import jakarta.validation.constraints.NotNull;

public record AdicionarIntegranteRequest(
    @NotNull(message = "O ID do usuário é obrigatório.")
    Integer usuarioId,

    @NotNull(message = "O tipo de integrante é obrigatório.")
    String tipoIntegrante
) {}
