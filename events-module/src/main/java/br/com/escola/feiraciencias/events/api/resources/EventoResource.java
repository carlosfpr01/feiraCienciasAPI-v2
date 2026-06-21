package br.com.escola.feiraciencias.events.api.resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import br.com.escola.feiraciencias.events.api.dto.requests.AtualizarCapaEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.requests.AtualizarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.requests.CriarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.responses.EventoResponse;
import br.com.escola.feiraciencias.events.api.mappers.EventoApiMapper;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import br.com.escola.feiraciencias.events.application.usecases.CriarEventoUseCase;
import br.com.escola.feiraciencias.events.application.usecases.BuscarEventoPorIdUseCase;
import br.com.escola.feiraciencias.events.application.usecases.ListarEventosUseCase;
import br.com.escola.feiraciencias.events.application.usecases.AtualizarEventoUseCase;
import br.com.escola.feiraciencias.events.application.usecases.AtualizarCapaEventoUseCase;
import br.com.escola.feiraciencias.events.application.usecases.RemoverCapaEventoUseCase;
import br.com.escola.feiraciencias.events.application.usecases.ExcluirEventoUseCase;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.PageResponse;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
public class EventoResource {

    @Inject
    ListarEventosUseCase listarEventosUseCase;

    @Inject
    BuscarEventoPorIdUseCase buscarEventoPorIdUseCase;

    @Inject
    CriarEventoUseCase criarEventoUseCase;

    @Inject
    AtualizarEventoUseCase atualizarEventoUseCase;

    @Inject
    AtualizarCapaEventoUseCase atualizarCapaEventoUseCase;

    @Inject
    RemoverCapaEventoUseCase removerCapaEventoUseCase;

    @Inject
    ExcluirEventoUseCase excluirEventoUseCase;

    @Inject
    EventoApiMapper mapper;

    @Inject
    JsonWebToken jwt;

    // ==================== LISTAGEM / DETALHE ====================

    @GET
    public Response listarEventos(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        Page<Evento> resultado = listarEventosUseCase.execute(page, size);
        List<EventoResponse> content = resultado.content().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(new PageResponse<>(content, page, size, resultado.total())).build();
    }

    @GET
    @Path("/{id}")
    public Response obterEvento(@PathParam("id") Integer id) {
        var evento = buscarEventoPorIdUseCase.execute(id);
        return Response.ok(mapper.toResponse(evento)).build();
    }

    // ==================== CRIAÇÃO (multipart com capa opcional) ====================

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response criarEvento(@Valid CriarEventoRequest eventoRequest) {
        Integer professorId = Integer.parseInt(jwt.getSubject());

        Evento evento = mapper.toDomain(eventoRequest);
        StorageFileInput capaInput = mapper.toStorageFileInput(eventoRequest.capa, "events");

        var criado = criarEventoUseCase.execute(evento, professorId, capaInput);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    // ==================== ATUALIZAÇÃO (JSON) ====================

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response atualizarEvento(@PathParam("id") Integer id, AtualizarEventoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());

        var evento = mapper.toDomain(request);
        var atualizado = atualizarEventoUseCase.execute(id, evento, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    // ==================== CAPA DE EVENTO ====================

    @PUT
    @Path("/{id}/capa")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response atualizarCapa(
            @PathParam("id") Integer id,
            @Valid AtualizarCapaEventoRequest request) {

        Integer professorId = Integer.parseInt(jwt.getSubject());

        FileUpload capa = request.capa;
        if (capa == null || capa.uploadedFile() == null) {
            throw new BusinessRuleException("O arquivo da capa é obrigatório.");
        }

        StorageFileInput capaInput = mapper.toStorageFileInput(capa, "events/" + id + "/capa");
        if (capaInput == null) {
            throw new BusinessRuleException("Falha ao processar o arquivo da capa.");
        }

        var atualizado = atualizarCapaEventoUseCase.execute(id, capaInput, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}/capa")
    @RolesAllowed("ADMIN")
    public Response removerCapa(@PathParam("id") Integer id) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var atualizado = removerCapaEventoUseCase.execute(id, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    // ==================== EXCLUSÃO ====================

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response excluirEvento(@PathParam("id") Integer id) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        excluirEventoUseCase.execute(id, professorId);
        return Response.noContent().build();
    }
}
