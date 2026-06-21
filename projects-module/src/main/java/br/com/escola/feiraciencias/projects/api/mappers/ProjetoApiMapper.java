package br.com.escola.feiraciencias.projects.api.mappers;

import br.com.escola.feiraciencias.projects.api.dto.requests.CriarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarComentarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.responses.ProjetoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.ComentarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioArquivoResponse;
import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public abstract class ProjetoApiMapper {

    @Inject
    protected StorageService storageService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", expression = "java(br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto.ATIVO)")
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "imagemCapaChave", ignore = true)
    public abstract Projeto toDomain(CriarProjetoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "imagemCapaChave", ignore = true)
    public abstract Projeto toDomain(AtualizarProjetoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataComentario", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "projetoId", ignore = true)
    public abstract Comentario toDomain(CriarComentarioRequest dto);

    public ProjetoResponse toResponse(Projeto domain) {
        if (domain == null) {
            return null;
        }

        String capaUrl = storageService.gerarUrl(domain.getImagemCapaChave());
        return new ProjetoResponse(
                domain.getId(),
                domain.getTitulo(),
                domain.getDescricao(),
                domain.getMateriais(),
                domain.getDataCriacao(),
                domain.getDataApresentacao(),
                domain.getSituacao(),
                domain.getAreaDeConhecimento(),
                domain.getSerie(),
                domain.getCriadoPorId(),
                domain.getEventoId(),
                capaUrl
        );
    }
    
    public abstract ComentarioResponse toResponse(Comentario domain);

    @Mapping(target = "arquivos", source = "arquivoChaves")
    public abstract RegistroDiarioResponse toResponse(RegistroDiario domain);

    protected List<RegistroDiarioArquivoResponse> mapArquivoChaves(List<String> arquivoChaves) {
        if (arquivoChaves == null) {
            return List.of();
        }
        return arquivoChaves.stream()
                .map(chave -> new RegistroDiarioArquivoResponse(chave, storageService.gerarUrl(chave)))
                .collect(Collectors.toList());
    }

    public StorageFileInput toStorageFileInput(FileUpload fileUpload, String prefixo) {
        if (fileUpload == null || fileUpload.uploadedFile() == null) {
            return null;
        }

        try {
            byte[] conteudo = java.nio.file.Files.readAllBytes(fileUpload.uploadedFile());
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
        } catch (java.io.IOException e) {
            throw new RuntimeException("Falha ao processar arquivo de upload.", e);
        }
    }
}
