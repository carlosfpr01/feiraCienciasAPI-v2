package br.com.escola.feiraciencias.events.api.mappers;

import br.com.escola.feiraciencias.events.api.dto.requests.AtualizarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.requests.CriarEventoRequest;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.api.dto.responses.EventoResponse;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.io.IOException;
import java.nio.file.Files;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public abstract class EventoApiMapper {

    @Inject
    protected StorageService storageService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "imagemCapaChave", ignore = true)
    @Mapping(target = "situacao", ignore = true)
    @Mapping(target = "dataInicio", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "dataFim", dateFormat = "yyyy-MM-dd")
    public abstract Evento toDomain(CriarEventoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "imagemCapaChave", ignore = true)
    public abstract Evento toDomain(AtualizarEventoRequest dto);

    /**
     * Converte o modelo de domínio para response, resolvendo a URL da capa na camada API.
     */
    public EventoResponse toResponse(Evento evento) {
        if (evento == null) {
            return null;
        }
        
        String capaUrl = storageService.gerarUrl(evento.getImagemCapaChave());
        return new EventoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getSituacao(),
                evento.getDataCriacao(),
                evento.getCriadoPorId(),
                capaUrl
        );
    }

    /**
     * Extrai o conteúdo de um FileUpload para um StorageFileInput.
     * Retorna null se o upload for nulo ou vazio.
     */
    public StorageFileInput toStorageFileInput(FileUpload fileUpload, String prefixo) {
        if (fileUpload == null || fileUpload.uploadedFile() == null) {
            return null;
        }

        try {
            byte[] conteudo = Files.readAllBytes(fileUpload.uploadedFile());
            if (conteudo.length == 0) {
                return null;
            }

            String mimeType = fileUpload.contentType() != null
                    ? fileUpload.contentType()
                    : "application/octet-stream";

            return new StorageFileInput(
                    fileUpload.fileName(),
                    mimeType,
                    conteudo.length,
                    conteudo,
                    prefixo
            );
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar arquivo de upload.", e);
        }
    }
}
