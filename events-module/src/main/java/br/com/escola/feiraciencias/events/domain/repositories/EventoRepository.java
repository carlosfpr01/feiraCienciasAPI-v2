package br.com.escola.feiraciencias.events.domain.repositories;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import java.util.Optional;
import java.util.List;

public interface EventoRepository {
    Evento salvar(Evento evento);
    Optional<Evento> buscarPorId(Integer id);
    List<Evento> listarTodos();
    Page<Evento> listarPaginado(int page, int size);
    void excluir(Integer id);
}
