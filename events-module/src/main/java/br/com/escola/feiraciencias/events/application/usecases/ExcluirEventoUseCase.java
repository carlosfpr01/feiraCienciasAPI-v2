package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.application.services.EventoService;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExcluirEventoUseCase {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    StorageService storageService;

    @Inject
    EventoService eventoService;

    @Transactional
    public void execute(Integer id, Integer professorId) {
        Evento evento = eventoService.buscarPorIdOuFalhar(id);

        if (evento.getImagemCapaChave() != null) {
            storageService.delete(evento.getImagemCapaChave());
        }

        eventoRepository.excluir(id);
    }
}
