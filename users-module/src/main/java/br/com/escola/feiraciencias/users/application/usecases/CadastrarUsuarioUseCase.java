package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CadastrarUsuarioUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public Usuario execute(String token, String email, String senha) {
        ConviteRegistro convite = conviteRepository.buscarPorToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Convite inválido ou não encontrado."));

        convite.usar(); // Valida expiração, status pendente e seta como USADO
        conviteRepository.salvar(convite);

        usuarioService.verificarEmailDisponivel(email);

        Usuario usuario = convite.criarUsuario(email);
        usuario.registrar(passwordService.hash(senha));

        return usuarioRepository.salvar(usuario);
    }


}
