package br.com.escola.feiraciencias.users.api.resources;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.PageResponse;
import br.com.escola.feiraciencias.users.api.dto.requests.GerarConviteAlunoRequest;
import br.com.escola.feiraciencias.users.api.dto.requests.GerarConviteProfessorRequest;
import br.com.escola.feiraciencias.users.api.dto.responses.ConviteResponse;
import br.com.escola.feiraciencias.users.application.usecases.CancelarConviteUseCase;
import br.com.escola.feiraciencias.users.application.usecases.GerarConviteAlunoUseCase;
import br.com.escola.feiraciencias.users.application.usecases.GerarConviteProfessorUseCase;
import br.com.escola.feiraciencias.users.application.usecases.ListarConvitesUseCase;
import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/convites")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConviteResource {

    @Inject JsonWebToken jwt;
    @Inject ListarConvitesUseCase listarConvitesUseCase;
    @Inject CancelarConviteUseCase cancelarConviteUseCase;
    @Inject GerarConviteAlunoUseCase gerarConviteAlunoUseCase;
    @Inject GerarConviteProfessorUseCase gerarConviteProfessorUseCase;

    @GET
    @RolesAllowed({"ADMIN", "PROFESSOR"})
    public Response listar(
            @QueryParam("status") StatusConvite status,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        Integer solicitanteId = Integer.parseInt(jwt.getSubject());
 
        Page<ConviteRegistro> resultado = listarConvitesUseCase.execute(
                solicitanteId, status, page, size);

        List<ConviteResponse> content = resultado.content().stream()
                .map(ConviteResponse::from).toList();

        return Response.ok(new PageResponse<>(content, page, size, resultado.total())).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "PROFESSOR"})
    public Response cancelar(@PathParam("id") Integer conviteId) {
        Integer solicitanteId = Integer.parseInt(jwt.getSubject());
 
        cancelarConviteUseCase.execute(solicitanteId, conviteId);
        return Response.noContent().build();
    }

       // ==========================================
    // GERAÇÃO DE CONVITES
    // ==========================================

    @POST
    @Path("/alunos")
    @RolesAllowed("PROFESSOR")
    public Response gerarConviteAluno(@Valid GerarConviteAlunoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        String token = gerarConviteAlunoUseCase.execute(
                request.nome(), request.matricula(), request.anoEscolar(), professorId);
        return Response.status(Response.Status.CREATED).entity(Map.of("token", token)).build();
    }

    @POST
    @Path("/professores")
    @RolesAllowed("ADMIN")
    public Response gerarConviteProfessor(@Valid GerarConviteProfessorRequest request) {
        Integer adminId = Integer.parseInt(jwt.getSubject());
        String token = gerarConviteProfessorUseCase.execute(
                request.nome(), request.disciplina(), adminId);
        return Response.status(Response.Status.CREATED).entity(Map.of("token", token)).build();
    }


}
