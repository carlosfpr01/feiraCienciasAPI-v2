package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AlterarSenhaUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public void execute(Integer usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioService.buscarPorIdOuFalhar(usuarioId);

        if (senhaAtual == null || !passwordService.verify(senhaAtual, usuario.getSenha())) {
            throw new BusinessRuleException("Senha atual incorreta.");
        }

        usuario.resetarSenha(passwordService.hash(novaSenha));
        usuarioRepository.salvar(usuario);
    }
}
