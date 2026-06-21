package br.com.escola.feiraciencias.projects.api.dto.requests;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.core.MediaType;

public class CriarRegistroDiarioRequest {

    @RestForm("texto")
    @NotBlank(message = "O texto do registro é obrigatório.")
    @Size(min = 1, max = 5000, message = "O texto deve ter entre 1 e 5000 caracteres.")
    public String texto;

    @RestForm("arquivos")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(
        type = SchemaType.ARRAY,
        implementation = String.class,
        format = "binary"
    )
    public List<FileUpload> arquivos;
}
