package br.com.escola.feiraciencias.events.api.dto.requests;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AtualizarEventoRequest(
    @NotBlank(message = "O nome do evento é obrigatório.")
    @Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres.")
    String nome,

    @Size(max = 5000, message = "A descrição não pode exceder 5000 caracteres.")
    String descricao,

    @NotNull(message = "A data de início é obrigatória.")
    LocalDate dataInicio,

    @NotNull(message = "A data de fim é obrigatória.")
    LocalDate dataFim,

    @NotNull(message = "A situação é obrigatória.")
    SituacaoEvento situacao
) {}
