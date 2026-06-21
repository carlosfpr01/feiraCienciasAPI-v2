package br.com.escola.feiraciencias.projects.api.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import br.com.escola.feiraciencias.projects.api.dto.requests.AdicionarIntegranteRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoMateriaisDescricaoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarRegistroDiarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarCapaProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AdicionarArquivoRegistroRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarComentarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarRegistroDiarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.responses.ComentarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.IntegranteResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.ProjetoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioResponse;
import br.com.escola.feiraciencias.projects.api.mappers.ProjetoApiMapper;
import br.com.escola.feiraciencias.projects.application.usecases.AdicionarArquivoRegistroUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AdicionarComentarioUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AdicionarIntegranteUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AtualizarCapaProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AtualizarMateriaisDescricaoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AtualizarProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.AtualizarRegistroDiarioUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.BuscarProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.CriarProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.CriarRegistroDiarioUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.ExcluirProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.ListarComentariosUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.ListarIntegrantesUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.ListarProjetosUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.ListarRegistrosDiariosUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.RemoverCapaProjetoUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.RemoverArquivoRegistroUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.RemoverComentarioUseCase;
import br.com.escola.feiraciencias.projects.application.usecases.RemoverIntegranteUseCase;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoResource {

    @Inject
    CriarProjetoUseCase criarProjetoUseCase;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    ListarProjetosUseCase listarProjetosUseCase;

    @Inject
    AtualizarProjetoUseCase atualizarProjetoUseCase;

    @Inject
    AtualizarCapaProjetoUseCase atualizarCapaProjetoUseCase;

    @Inject
    RemoverCapaProjetoUseCase removerCapaProjetoUseCase;

    @Inject
    AtualizarMateriaisDescricaoUseCase atualizarMateriaisDescricaoUseCase;

    @Inject
    ExcluirProjetoUseCase excluirProjetoUseCase;

    @Inject
    AdicionarIntegranteUseCase adicionarIntegranteUseCase;

    @Inject
    ListarIntegrantesUseCase listarIntegrantesUseCase;

    @Inject
    AdicionarComentarioUseCase adicionarComentarioUseCase;

    @Inject
    ListarComentariosUseCase listarComentariosUseCase;

    @Inject
    CriarRegistroDiarioUseCase criarRegistroDiarioUseCase;

    @Inject
    AtualizarRegistroDiarioUseCase atualizarRegistroDiarioUseCase;

    @Inject
    ListarRegistrosDiariosUseCase listarRegistrosDiariosUseCase;

    @Inject
    AdicionarArquivoRegistroUseCase adicionarArquivoRegistroUseCase;

    @Inject
    RemoverArquivoRegistroUseCase removerArquivoRegistroUseCase;

    @Inject
    RemoverComentarioUseCase removerComentarioUseCase;

    @Inject
    RemoverIntegranteUseCase removerIntegranteUseCase;

    @Inject
    ProjetoApiMapper mapper;

    @Inject
    JsonWebToken jwt;

    // ==================== PROJETOS CRUD ====================

    @POST
    @RolesAllowed({"ADMIN","PROFESSOR"})
    public Response criarProjeto(@Valid CriarProjetoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var projeto = mapper.toDomain(request);
        var criado = criarProjetoUseCase.execute(projeto, professorId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}")
    public Response obterProjeto(@PathParam("id") Integer id) {
        var projeto = buscarProjetoUseCase.execute(id);
        return Response.ok(mapper.toResponse(projeto)).build();
    }

    @GET
    @Path("/evento/{eventoId}")
    public Response listarProjetosPorEvento(@PathParam("eventoId") Integer eventoId) {
        List<ProjetoResponse> projetos = listarProjetosUseCase.execute(eventoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(projetos).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"PROFESSOR","ADMIN"})
    public Response atualizarProjeto(@PathParam("id") Integer id, @Valid AtualizarProjetoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var projeto = mapper.toDomain(request);
        var atualizado = atualizarProjetoUseCase.execute(id, projeto, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @PATCH
    @Path("/{id}/materiais-descricao")
    public Response atualizarMateriaisDescricao(@PathParam("id") Integer id, 
                                               @Valid AtualizarProjetoMateriaisDescricaoRequest request) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        var atualizado = atualizarMateriaisDescricaoUseCase.execute(id, 
                                                                     request.descricao(), 
                                                                     request.materiais(), 
                                                                     usuarioId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"PROFESSOR","ADMIN"})
    public Response excluirProjeto(@PathParam("id") Integer id) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        excluirProjetoUseCase.execute(id, professorId);
        return Response.noContent().build();
    }

    // ==================== CAPA DO PROJETO ====================

    @PUT
    @Path("/{id}/capa")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"ADMIN","PROFESSOR","ALUNO"})
    public Response atualizarCapa(
            @PathParam("id") Integer id,
            @Valid AtualizarCapaProjetoRequest request) {

        Integer usuarioId = Integer.parseInt(jwt.getSubject());

        FileUpload capa = request.capa;
        if (capa == null || capa.uploadedFile() == null) {
            throw new BusinessRuleException("O arquivo da capa é obrigatório.");
        }

        StorageFileInput capaInput = mapper.toStorageFileInput(capa, "projects/" + id + "/capa");
        if (capaInput == null) {
            throw new BusinessRuleException("Falha ao processar o arquivo da capa.");
        }

        var atualizado = atualizarCapaProjetoUseCase.execute(id, capaInput, usuarioId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}/capa")
    @RolesAllowed({"ADMIN","PROFESSOR","ALUNO"})
    public Response removerCapa(@PathParam("id") Integer id) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        var atualizado = removerCapaProjetoUseCase.execute(id, usuarioId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    // ==================== INTEGRANTES ====================

    @POST
    @Path("/{id}/integrantes")
    @RolesAllowed({"PROFESSOR","ADMIN"})
    public Response adicionarIntegrante(@PathParam("id") Integer id, @Valid AdicionarIntegranteRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var integrante = adicionarIntegranteUseCase.execute(id, request.usuarioId(), request.tipoIntegrante(), professorId);
        return Response.status(Response.Status.CREATED)
                .entity(new IntegranteResponse(integrante.getId(), 
                                              integrante.getProjetoId(), 
                                              integrante.getUsuarioId(),
                                              integrante.getTipoIntegrante().name(),
                                              integrante.getDataVinculo()))
                .build();
    }

    @GET
    @Path("/{id}/integrantes")
    public Response listarIntegrantes(@PathParam("id") Integer id) {
        List<IntegranteResponse> integrantes = listarIntegrantesUseCase.execute(id).stream()
                .map(i -> new IntegranteResponse(i.getId(), i.getProjetoId(), i.getUsuarioId(), 
                                                i.getTipoIntegrante().name(), i.getDataVinculo()))
                .collect(Collectors.toList());
        return Response.ok(integrantes).build();
    }

    @DELETE
    @Path("/{id}/integrantes/{integranteId}")
    @RolesAllowed({"ADMIN", "PROFESSOR", "ALUNO"})
    public Response removerIntegrante(@PathParam("id") Integer id, @PathParam("integranteId") Integer integranteId) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        removerIntegranteUseCase.execute(integranteId, usuarioId);
        return Response.noContent().build();
    }

    // ==================== COMENTÁRIOS ====================

    @POST
    @Path("/{id}/comentarios")
    @RolesAllowed({"ADMIN","PROFESSOR", "ALUNO"})
    public Response adicionarComentario(@PathParam("id") Integer id, @Valid CriarComentarioRequest request) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        var comentario = mapper.toDomain(request);
        var criado = adicionarComentarioUseCase.execute(id, comentario, usuarioId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}/comentarios")
    public Response listarComentarios(@PathParam("id") Integer id) {
        List<ComentarioResponse> comentarios = listarComentariosUseCase.execute(id).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(comentarios).build();
    }

    @DELETE
    @Path("/{id}/comentarios/{comentarioId}")
    @RolesAllowed({"ADMIN", "PROFESSOR", "ALUNO"})
    public Response removerComentario(@PathParam("id") Integer id, @PathParam("comentarioId") Integer comentarioId) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        removerComentarioUseCase.execute(comentarioId, usuarioId);
        return Response.noContent().build();
    }

    // ==================== REGISTROS DIÁRIOS ====================

    @POST
    @Path("/{id}/registros-diarios")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ALUNO")
    public Response criarRegistroDiario(
            @PathParam("id") Integer projetoId,
            @Valid CriarRegistroDiarioRequest request) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        
        RegistroDiario registro = new RegistroDiario();
        registro.setTexto(request.texto);

        List<StorageFileInput> arquivosInput = new ArrayList<>();
        if (request.arquivos != null) {
            for (FileUpload fileUpload : request.arquivos) {
                StorageFileInput input = mapper.toStorageFileInput(fileUpload, "projects/" + projetoId + "/registros");
                if (input != null) {
                    arquivosInput.add(input);
                }
            }
        }

        var criado = criarRegistroDiarioUseCase.execute(projetoId, registro, alunoId, arquivosInput);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}/registros-diarios")
    @RolesAllowed({"ADMIN", "PROFESSOR", "ALUNO"})
    public Response listarRegistrosDiarios(@PathParam("id") Integer id) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        List<RegistroDiarioResponse> registros = listarRegistrosDiariosUseCase.execute(id, usuarioId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(registros).build();
    }

    @PUT
    @Path("/{id}/registros-diarios/{registroId}")
    @RolesAllowed("ALUNO")
    public Response atualizarRegistroDiario(
            @PathParam("id") Integer projetoId,
            @PathParam("registroId") Integer registroId,
            @Valid AtualizarRegistroDiarioRequest request) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        var atualizado = atualizarRegistroDiarioUseCase.execute(registroId, request.texto(), alunoId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @POST
    @Path("/{id}/registros-diarios/{registroId}/arquivos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ALUNO")
    public Response adicionarArquivoRegistro(
            @PathParam("id") Integer projetoId,
            @PathParam("registroId") Integer registroId,
            @Valid AdicionarArquivoRegistroRequest request) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());

        FileUpload arquivo = request.arquivo;
        if (arquivo == null || arquivo.uploadedFile() == null) {
            throw new BusinessRuleException("O arquivo é obrigatório.");
        }

        StorageFileInput input = mapper.toStorageFileInput(arquivo, "projects/" + projetoId + "/registros");
        if (input == null) {
            throw new BusinessRuleException("Falha ao processar o arquivo.");
        }

        var atualizado = adicionarArquivoRegistroUseCase.execute(registroId, input, alunoId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}/registros-diarios/{registroId}/arquivos/{chave}")
    @RolesAllowed("ALUNO")
    public Response removerArquivoRegistro(
            @PathParam("id") Integer projetoId,
            @PathParam("registroId") Integer registroId,
            @PathParam("chave") String chave) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        var atualizado = removerArquivoRegistroUseCase.execute(registroId, chave, alunoId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }
}
