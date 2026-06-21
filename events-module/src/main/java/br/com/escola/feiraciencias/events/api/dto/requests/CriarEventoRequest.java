package br.com.escola.feiraciencias.events.api.dto.requests;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.core.MediaType;

public class CriarEventoRequest {

    @RestForm("nome")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    public String nome;

    @RestForm("descricao")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    public String descricao;

    @RestForm("dataInicio")
    @NotNull(message = "Data de início é obrigatória")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data de início deve estar no formato yyyy-MM-dd")
    public String dataInicio;

    @RestForm("dataFim")
    @NotNull(message = "Data de fim é obrigatória")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data de fim deve estar no formato yyyy-MM-dd")
    public String dataFim;

    @RestForm("capa")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = SchemaType.STRING, format = "binary", implementation = String.class)
    public FileUpload capa;

    @AssertTrue(message = "Data de início deve ser maior que a data atual")
    @JsonIgnore
    @Schema(hidden = true)
    public boolean isDataInicioValida() {
        if (dataInicio == null) return true;
        try {
            return LocalDate.parse(dataInicio).isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    @AssertTrue(message = "Data de fim deve ser após a data de início")
    @JsonIgnore
    @Schema(hidden = true)
    public boolean isDataFimValida() {
        if (dataInicio == null || dataFim == null) return true;
        try {
            return LocalDate.parse(dataFim).isAfter(LocalDate.parse(dataInicio));
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}