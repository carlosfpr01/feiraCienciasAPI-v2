package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Transactional
    public Projeto execute(Integer id, Projeto projetoAtualizado, Integer professorId) {
        Projeto projeto = buscarProjetoUseCase.execute(id);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o criador do projeto pode atualizá-lo.");
        }

        projeto.setTitulo(projetoAtualizado.getTitulo());
        projeto.setDescricao(projetoAtualizado.getDescricao());
        projeto.setMateriais(projetoAtualizado.getMateriais());
        projeto.setAreaDeConhecimento(projetoAtualizado.getAreaDeConhecimento());
        projeto.setSerie(projetoAtualizado.getSerie());
        projeto.setDataApresentacao(projetoAtualizado.getDataApresentacao());
        projeto.setSituacao(projetoAtualizado.getSituacao());

        return projetoRepository.salvar(projeto);
    }
}
