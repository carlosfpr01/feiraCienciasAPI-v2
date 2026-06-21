package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.application.services.EventoService;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BuscarEventoPorIdUseCase {

    @Inject
    EventoService eventoService;

    public Evento execute(Integer id) {
        return eventoService.buscarPorIdOuFalhar(id);
    }
}
