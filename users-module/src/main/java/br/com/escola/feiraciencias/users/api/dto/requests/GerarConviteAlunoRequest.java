package br.com.escola.feiraciencias.users.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GerarConviteAlunoRequest(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    String nome,

    @NotBlank(message = "A matrícula é obrigatória.")
    String matricula,

    @NotBlank(message = "O ano escolar é obrigatório.")
    String anoEscolar
) {}
