package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BuscarProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    public Projeto execute(Integer id) {
        return projetoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));
    }
}
