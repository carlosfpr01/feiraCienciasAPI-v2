package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import br.com.escola.feiraciencias.storage.application.dto.StorageUploadResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarCapaProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    StorageService storageService;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Transactional
    public Projeto execute(Integer id, StorageFileInput capaInput, Integer usuarioId) {
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
            throw new BusinessRuleException("Apenas o criador ou alunos integrantes do projeto podem alterar a capa.");
        }

        if (projeto.getImagemCapaChave() != null) {
            storageService.delete(projeto.getImagemCapaChave());
        }

        StorageUploadResult resultado = storageService.upload(capaInput, usuarioId);
        projeto.setImagemCapaChave(resultado.chave());

        return projetoRepository.salvar(projeto);
    }
}
