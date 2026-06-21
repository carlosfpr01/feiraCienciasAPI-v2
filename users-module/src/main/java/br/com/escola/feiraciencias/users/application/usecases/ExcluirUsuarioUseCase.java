package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExcluirUsuarioUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void execute(Integer solicitanteId, Integer targetId) {
        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        Usuario target = usuarioService.buscarPorIdOuFalhar(targetId);

        switch (solicitante.getTipoUsuario()) {
            case ALUNO -> throw new BusinessRuleException("Alunos nao podem excluir usuarios.");
            case PROFESSOR -> {
                if (!target.isAluno() || !solicitanteId.equals(target.getCriadoPorId())) {
                    throw new BusinessRuleException(
                            "Professores so podem excluir alunos vinculados a eles.");
                }
            }
            case ADMIN -> {
                if (target.isAdmin()) {
                    throw new BusinessRuleException(
                            "Administradores nao podem excluir outros administradores.");
                }
            }
        }

        usuarioRepository.deletar(targetId);
    }
}
