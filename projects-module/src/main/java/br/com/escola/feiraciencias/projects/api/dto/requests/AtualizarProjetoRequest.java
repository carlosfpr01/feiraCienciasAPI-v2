package br.com.escola.feiraciencias.projects.api.dto.requests;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AtualizarProjetoRequest(
    @NotBlank(message = "O título do projeto é obrigatório.")
    @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres.")
    String titulo,

    @Size(max = 5000, message = "A descrição não pode exceder 5000 caracteres.")
    String descricao,

    @Size(max = 5000, message = "Os materiais não podem exceder 5000 caracteres.")
    String materiais,

    @NotNull(message = "A área de conhecimento é obrigatória.")
    @Size(min = 3, max = 100, message = "A área deve ter entre 3 e 100 caracteres.")
    String areaDeConhecimento,

    @NotBlank(message = "A série é obrigatória.")
    @Size(min = 1, max = 50, message = "A série deve ter entre 1 e 50 caracteres.")
    String serie,

    @NotNull(message = "A data de apresentação é obrigatória.")
    LocalDate dataApresentacao,

    @NotNull(message = "A situação do projeto é obrigatória.")
    SituacaoProjeto situacao
) {}
