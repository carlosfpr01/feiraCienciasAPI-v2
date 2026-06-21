package br.com.escola.feiraciencias.events.api.dto.requests;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;

public class AtualizarCapaEventoRequest {

    @RestForm("capa")
    @NotNull(message = "O arquivo da capa é obrigatório.")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = SchemaType.STRING, format = "binary", implementation = String.class, description = "Arquivo da capa do evento")
    public FileUpload capa;
}
