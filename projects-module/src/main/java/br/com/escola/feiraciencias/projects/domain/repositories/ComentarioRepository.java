package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import java.util.List;
import java.util.Optional;

public interface ComentarioRepository {
    Comentario salvar(Comentario comentario);
    Optional<Comentario> buscarPorId(Integer id);
    List<Comentario> listarPorProjeto(Integer projetoId);
    void excluir(Integer id);
}
