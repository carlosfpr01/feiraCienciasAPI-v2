package br.com.escola.feiraciencias.projects.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarComentarioRequest(
    @NotBlank(message = "O comentário não pode estar vazio.")
    @Size(min = 1, max = 5000, message = "O comentário deve ter entre 1 e 5000 caracteres.")
    String texto
) {}
