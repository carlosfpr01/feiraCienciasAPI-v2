package br.com.escola.feiraciencias.projects.application.usecases;

import java.util.List;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListarProjetosUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    br.com.escola.feiraciencias.events.application.services.EventoService eventoService;

    public List<Projeto> execute(Integer eventoId) {
        // Verifica se o evento existe, lançando EntityNotFoundException caso contrário
        eventoService.buscarPorIdOuFalhar(eventoId);
        return projetoRepository.listarPorEvento(eventoId);
    }
}
