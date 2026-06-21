package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdicionarArquivoRegistroUseCase {

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    StorageService storageService;

    @Transactional
    public RegistroDiario execute(Integer registroId, StorageFileInput arquivoInput, Integer alunoId) {
        var registro = registroDiarioRepository.buscarPorId(registroId)
                .orElseThrow(() -> new EntityNotFoundException("Registro diário não encontrado."));

        if (!registro.getCriadoPorId().equals(alunoId)) {
            throw new BusinessRuleException("Apenas o autor do registro pode adicionar arquivos.");
        }

        var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(registro.getProjetoId(), alunoId);
        if (vinculo.isEmpty()) {
            throw new BusinessRuleException("O aluno precisa ser integrante do projeto para adicionar arquivos no diário.");
        }

        if (registro.getArquivoChaves().size() >= RegistroDiario.LIMITE_ARQUIVOS) {
            throw new BusinessRuleException("Limite de " + RegistroDiario.LIMITE_ARQUIVOS + " arquivos por registro atingido.");
        }

        var resultado = storageService.upload(arquivoInput, alunoId);
        registro.adicionarArquivoChave(resultado.chave());

        return registroDiarioRepository.salvar(registro);
    }
}
