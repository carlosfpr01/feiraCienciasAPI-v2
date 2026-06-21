package br.com.escola.feiraciencias.events.application.services;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventoService {

    @Inject
    EventoRepository eventoRepository;

    public Evento buscarPorIdOuFalhar(Integer id) {
        return eventoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
    }
}
