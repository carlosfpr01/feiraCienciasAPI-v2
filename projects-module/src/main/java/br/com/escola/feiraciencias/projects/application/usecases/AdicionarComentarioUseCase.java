package br.com.escola.feiraciencias.projects.application.usecases;

import java.time.LocalDateTime;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.repositories.ComentarioRepository;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdicionarComentarioUseCase {

    @Inject
    ComentarioRepository comentarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public Comentario execute(Integer projetoId, Comentario comentario, Integer usuarioId) {
        buscarProjetoUseCase.execute(projetoId);
        usuarioService.buscarUsuarioPorId(usuarioId);

        comentario.setProjetoId(projetoId);
        comentario.setCriadoPorId(usuarioId);
        comentario.setDataComentario(LocalDateTime.now());

        return comentarioRepository.salvar(comentario);
    }
}
