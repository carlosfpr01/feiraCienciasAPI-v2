package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
public class ResetarSenhaUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public String execute(Integer solicitanteId, Integer targetId) {
        if (solicitanteId.equals(targetId)) {
            throw new BusinessRuleException("Nao e possivel resetar a propria senha por este fluxo. Utilize a alteracao de senha propria.");
        }

        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        Usuario target = usuarioService.buscarPorIdOuFalhar(targetId);

        switch (solicitante.getTipoUsuario()) {
            case ALUNO -> throw new BusinessRuleException("Alunos nao podem resetar a senha de outros usuarios.");
            case PROFESSOR -> {
                if (!target.isAluno() || !solicitanteId.equals(target.getCriadoPorId())) {
                    throw new BusinessRuleException("Professores so podem resetar a senha de alunos vinculados a eles.");
                }
            }
            case ADMIN -> {
                if (target.isAdmin()) {
                    throw new BusinessRuleException("Administradores nao podem resetar a senha de outros administradores.");
                }
            }
        }

        String novaSenhaPlain = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        target.resetarSenha(passwordService.hash(novaSenhaPlain));
        usuarioRepository.salvar(target);

        return novaSenhaPlain;
    }
}
