package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.escola.feiraciencias.users.application.services.UsuarioService;

@ApplicationScoped
public class ListarUsuariosUseCase {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioService usuarioService;

    /**
     * ADMIN: lista por tipo (null = todos).
     * PROFESSOR: lista somente seus alunos vinculados.
     */
    public Page<Usuario> execute(Integer solicitanteId,
                                  TipoUsuario filtroTipo, int page, int size) {
        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        if (solicitante.getTipoUsuario() == TipoUsuario.ADMIN) {
            return usuarioRepository.listarPorTipo(filtroTipo, page, size);
        }
        // PROFESSOR: ignora filtroTipo, lista apenas seus alunos
        return usuarioRepository.listarAlunosPorProfessor(solicitanteId, page, size);
    }
}
