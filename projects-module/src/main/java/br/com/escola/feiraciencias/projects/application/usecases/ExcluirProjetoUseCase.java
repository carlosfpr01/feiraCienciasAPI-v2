package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExcluirProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    StorageService storageService;

    @Transactional
    public void execute(Integer id, Integer professorId) {
        var projeto = buscarProjetoUseCase.execute(id);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o criador do projeto pode deletá-lo.");
        }

        if (projeto.getImagemCapaChave() != null) {
            storageService.delete(projeto.getImagemCapaChave());
        }

        projetoRepository.excluir(id);
    }
}
