package br.com.escola.feiraciencias.projects.application.usecases;

import java.util.List;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListarRegistrosDiariosUseCase {

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    UsuarioService usuarioService;

    public List<RegistroDiario> execute(Integer projetoId, Integer usuarioId) {
        var projeto = buscarProjetoUseCase.execute(projetoId);
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
            return registroDiarioRepository.listarPorProjeto(projetoId);
        }

        if (projeto.getCriadoPorId().equals(usuarioId)) {
            return registroDiarioRepository.listarPorProjeto(projetoId);
        }

        var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(projetoId, usuarioId);
        if (vinculo.isPresent()) {
            return registroDiarioRepository.listarPorProjeto(projetoId);
        }

        throw new BusinessRuleException("Apenas o criador ou integrantes do projeto podem visualizar os registros diários.");
    }
}
