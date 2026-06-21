package br.com.escola.feiraciencias.events.application.usecases;

import java.util.List;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListarEventosUseCase {

    @Inject
    EventoRepository eventoRepository;

    public Page<Evento> execute(int page, int size) {
        return eventoRepository.listarPaginado(page, size);
    }
}
