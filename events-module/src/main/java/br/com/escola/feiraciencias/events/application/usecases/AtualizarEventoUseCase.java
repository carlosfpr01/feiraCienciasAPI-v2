package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.application.services.EventoService;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarEventoUseCase {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    EventoService eventoService;

    @Transactional
    public Evento execute(Integer id, Evento eventoAtualizado, Integer professorId) {
        Evento evento = eventoService.buscarPorIdOuFalhar(id);

        evento.setNome(eventoAtualizado.getNome());
        evento.setDescricao(eventoAtualizado.getDescricao());
        evento.setDataInicio(eventoAtualizado.getDataInicio());
        evento.setDataFim(eventoAtualizado.getDataFim());
        evento.setSituacao(eventoAtualizado.getSituacao());

        return eventoRepository.salvar(evento);
    }
}
