package br.com.escola.feiraciencias.projects.application.usecases;

import java.util.List;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.repositories.ComentarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListarComentariosUseCase {

    @Inject
    ComentarioRepository comentarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    public List<Comentario> execute(Integer projetoId) {
        buscarProjetoUseCase.execute(projetoId);
        return comentarioRepository.listarPorProjeto(projetoId);
    }
}
