package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Usuario;

@ApplicationScoped
public class ListarConvitesUseCase {

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Inject
    UsuarioService usuarioService;

    /**
     * PROFESSOR: lista apenas os proprios convites.
     * ADMIN: lista todos (criadorId = null = sem filtro).
     */
    public Page<ConviteRegistro> execute(Integer solicitanteId,
                                          StatusConvite filtroStatus, int page, int size) {
        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        Integer criadorId = solicitante.getTipoUsuario() == TipoUsuario.PROFESSOR ? solicitanteId : null;
        return conviteRepository.listarPorCriador(criadorId, filtroStatus, page, size);
    }
}
