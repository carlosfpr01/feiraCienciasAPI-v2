package br.com.escola.feiraciencias.users.api.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.PageResponse;
import br.com.escola.feiraciencias.users.api.dto.requests.AlterarSenhaRequest;
import br.com.escola.feiraciencias.users.api.dto.requests.AtualizarUsuarioRequest;
import br.com.escola.feiraciencias.users.api.dto.requests.CadastrarUsuarioRequest;
import br.com.escola.feiraciencias.users.api.dto.responses.ResetSenhaResponse;
import br.com.escola.feiraciencias.users.api.dto.responses.UsuarioResponse;
import br.com.escola.feiraciencias.users.application.usecases.AlterarSenhaUseCase;
import br.com.escola.feiraciencias.users.application.usecases.AtualizarUsuarioUseCase;
import br.com.escola.feiraciencias.users.application.usecases.CadastrarUsuarioUseCase;
import br.com.escola.feiraciencias.users.application.usecases.ExcluirUsuarioUseCase;
import br.com.escola.feiraciencias.users.application.usecases.ListarUsuariosUseCase;
import br.com.escola.feiraciencias.users.application.usecases.ResetarSenhaUseCase;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject JsonWebToken jwt;

    @Inject CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    @Inject ListarUsuariosUseCase listarUsuariosUseCase;
    @Inject AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    @Inject AlterarSenhaUseCase alterarSenhaUseCase;
    @Inject ResetarSenhaUseCase resetarSenhaUseCase;
    @Inject ExcluirUsuarioUseCase excluirUsuarioUseCase;

    // ==========================================
    // LISTAGEM
    // ==========================================

    @GET
    @RolesAllowed({"ADMIN", "PROFESSOR"})
    public Response listar(
            @QueryParam("tipo") TipoUsuario tipo,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        Integer solicitanteId = Integer.parseInt(jwt.getSubject());

        Page<Usuario> resultado = listarUsuariosUseCase.execute(solicitanteId, tipo, page, size);
        List<UsuarioResponse> content = resultado.content().stream().map(UsuarioResponse::from).toList();
        return Response.ok(new PageResponse<>(content, page, size, resultado.total())).build();
    }


    // ==========================================
    // REGISTRO / ACEITE DE CONVITE
    // ==========================================

    @POST
    @PermitAll
    public Response registrarUsuario(@Valid CadastrarUsuarioRequest request) {
        cadastrarUsuarioUseCase.execute(request.token(), request.email(), request.senha());
        return Response.status(Response.Status.CREATED).build();
    }



    // ==========================================
    // ATUALIZAÇÃO DE DADOS CADASTRAIS
    // ==========================================

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "PROFESSOR", "ALUNO"})
    public Response atualizar(@PathParam("id") Integer targetId,
                               AtualizarUsuarioRequest request) {
        Integer solicitanteId = Integer.parseInt(jwt.getSubject());

        Usuario atualizado = atualizarUsuarioUseCase.execute(
                solicitanteId, targetId,
                request.nome(), request.email(),
                request.matricula(), request.anoEscolar(), request.materia());

        return Response.ok(UsuarioResponse.from(atualizado)).build();
    }

    @PATCH
    @Path("/{id}/senha")
    @RolesAllowed({"ADMIN", "PROFESSOR", "ALUNO"})
    public Response alterarSenha(@PathParam("id") Integer targetId,
                                  @Valid AlterarSenhaRequest request) {
        Integer solicitanteId = Integer.parseInt(jwt.getSubject());

        if (solicitanteId.equals(targetId)) {
            alterarSenhaUseCase.execute(solicitanteId, request.senhaAtual(), request.novaSenha());
            return Response.noContent().build();
        } else {
            String novaSenha = resetarSenhaUseCase.execute(solicitanteId, targetId);
            return Response.ok(new ResetSenhaResponse(novaSenha)).build();
        }
    }

    @POST
    @Path("/{id}/senha/reset")
    @RolesAllowed({ "ADMIN", "PROFESSOR" })
    public Response resetarSenha(@PathParam("id") Integer targetId) {
        Integer solicitanteId = Integer.parseInt(jwt.getSubject());

        String novaSenha = resetarSenhaUseCase.execute(solicitanteId, targetId);
        return Response.ok(new ResetSenhaResponse(novaSenha)).build();

    }

    // ==========================================
    // EXCLUSÃO
    // ==========================================

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "PROFESSOR"})
    public Response excluir(@PathParam("id") Integer targetId) {
        Integer solicitanteId = Integer.parseInt(jwt.getSubject());

        excluirUsuarioUseCase.execute(solicitanteId, targetId);
        return Response.noContent().build();
    }
}
