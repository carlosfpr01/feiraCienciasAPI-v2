package br.com.escola.feiraciencias.projects.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarRegistroDiarioRequest(
    @NotBlank(message = "O texto do registro é obrigatório.")
    @Size(min = 1, max = 5000, message = "O texto deve ter entre 1 e 5000 caracteres.")
    String texto
) {}
