package br.com.escola.feiraciencias.projects.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarProjetoMateriaisDescricaoRequest(
    @Size(max = 5000, message = "A descrição não pode exceder 5000 caracteres.")
    String descricao,

    @Size(max = 5000, message = "Os materiais não podem exceder 5000 caracteres.")
    String materiais
) {}
