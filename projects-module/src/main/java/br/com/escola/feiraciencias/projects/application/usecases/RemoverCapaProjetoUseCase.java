package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RemoverCapaProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    StorageService storageService;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Transactional
    public Projeto execute(Integer id, Integer usuarioId) {
        Projeto projeto = buscarProjetoUseCase.execute(id);

        boolean isCriador = projeto.getCriadoPorId().equals(usuarioId);
        boolean isAlunoIntegrante = false;

        if (!isCriador) {
            var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(id, usuarioId);
            if (vinculo.isPresent()) {
                isAlunoIntegrante = true;
            }
        }

        if (!isCriador && !isAlunoIntegrante) {
            throw new BusinessRuleException("Apenas o criador ou alunos integrantes do projeto podem remover a capa.");
        }

        if (projeto.getImagemCapaChave() != null) {
            storageService.delete(projeto.getImagemCapaChave());
            projeto.setImagemCapaChave(null);
            return projetoRepository.salvar(projeto);
        }

        return projeto;
    }
}
