package br.com.escola.feiraciencias.users.application.services;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public Usuario buscarPorIdOuFalhar(Integer id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado com o ID: " + id));
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    public Usuario buscarPorEmailOuFalhar(String email, String mensagemErro) {
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new BusinessRuleException(mensagemErro));
    }

    public void verificarEmailDisponivel(String email) {
        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new BusinessRuleException("O email " + email + " já está em uso.");
        }
    }
}
