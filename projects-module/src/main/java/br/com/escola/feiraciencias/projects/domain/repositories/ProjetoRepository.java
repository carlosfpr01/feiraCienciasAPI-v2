package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import java.util.Optional;
import java.util.List;

public interface ProjetoRepository {
    Projeto salvar(Projeto projeto);
    Optional<Projeto> buscarPorId(Integer id);
    List<Projeto> listarPorEvento(Integer eventoId);
    void excluir(Integer id);
}
