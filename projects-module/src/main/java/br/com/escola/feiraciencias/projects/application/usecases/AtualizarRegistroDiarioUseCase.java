package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarRegistroDiarioUseCase {

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Transactional
    public RegistroDiario execute(Integer registroId, String novoTexto, Integer alunoId) {
        var registro = registroDiarioRepository.buscarPorId(registroId)
                .orElseThrow(() -> new EntityNotFoundException("Registro diário não encontrado."));

        if (!registro.getCriadoPorId().equals(alunoId)) {
            throw new BusinessRuleException("Apenas o autor do registro pode atualizá-lo.");
        }

        var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(registro.getProjetoId(), alunoId);
        if (vinculo.isEmpty()) {
            throw new BusinessRuleException("O aluno precisa ser integrante do projeto para atualizar registros diários.");
        }

        registro.setTexto(novoTexto);
        return registroDiarioRepository.salvar(registro);
    }
}
