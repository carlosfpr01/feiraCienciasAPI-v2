package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RemoverArquivoRegistroUseCase {

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    StorageService storageService;

    @Transactional
    public RegistroDiario execute(Integer registroId, String chave, Integer alunoId) {
        var registro = registroDiarioRepository.buscarPorId(registroId)
                .orElseThrow(() -> new EntityNotFoundException("Registro diário não encontrado."));

        if (!registro.getCriadoPorId().equals(alunoId)) {
            throw new BusinessRuleException("Apenas o autor do registro pode remover arquivos.");
        }

        var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(registro.getProjetoId(), alunoId);
        if (vinculo.isEmpty()) {
            throw new BusinessRuleException("O aluno precisa ser integrante do projeto para remover arquivos no diário.");
        }

        if (registro.removerArquivoChave(chave)) {
            storageService.delete(chave);
            return registroDiarioRepository.salvar(registro);
        }

        throw new BusinessRuleException("Arquivo não encontrado neste registro.");
    }
}
