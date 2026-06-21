package br.com.escola.feiraciencias.users.application.usecases;

import java.time.Duration;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthUseCase {

    public record AuthResult(String token, Usuario usuario) {}

    @Inject
    UsuarioService usuarioService;

    @Inject
    PasswordService passwordService;

    public AuthResult execute(String email, String senha) {
        Usuario usuario = usuarioService.buscarPorEmailOuFalhar(email, "Credenciais inválidas.");

        if (!passwordService.verify(senha, usuario.getSenha())) {
            throw new BusinessRuleException("Credenciais inválidas.");
        }

        String token = Jwt.issuer("https://feiraciencias.escola.com.br")
                .subject(usuario.getId().toString())
                .groups(usuario.getTipoUsuario().name())
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .expiresIn(Duration.ofHours(4))
                .sign();

        return new AuthResult(token, usuario);
    }
}
