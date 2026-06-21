package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.repositories.ComentarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RemoverComentarioUseCase {

    @Inject
    ComentarioRepository comentarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public void execute(Integer comentarioId, Integer usuarioId) {
        var comentario = comentarioRepository.buscarPorId(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado."));
        
        var projeto = buscarProjetoUseCase.execute(comentario.getProjetoId());
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        boolean isAdmin = usuario.getTipoUsuario() == TipoUsuario.ADMIN;
        boolean isProfessorVinculado = projeto.getCriadoPorId().equals(usuarioId);
        boolean isAutor = comentario.getCriadoPorId().equals(usuarioId);

        if (!isAdmin && !isProfessorVinculado && !isAutor) {
            throw new BusinessRuleException("Apenas o autor, o professor vinculado ou um administrador podem remover este comentário.");
        }

        comentarioRepository.excluir(comentarioId);
    }
}
