package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RemoverIntegranteUseCase {

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public void execute(Integer integranteId, Integer usuarioId) {
        var integrante = projetoUsuarioRepository.buscarPorId(integranteId)
                .orElseThrow(() -> new EntityNotFoundException("Integrante não encontrado."));
        
        var projeto = buscarProjetoUseCase.execute(integrante.getProjetoId());
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        boolean isAdmin = usuario.getTipoUsuario() == TipoUsuario.ADMIN;
        boolean isProfessorVinculado = projeto.getCriadoPorId().equals(usuarioId);
        boolean isProprioIntegrante = integrante.getUsuarioId().equals(usuarioId);

        if (!isAdmin && !isProfessorVinculado && !isProprioIntegrante) {
            throw new BusinessRuleException("Apenas o próprio aluno, o professor vinculado ou um administrador podem remover este integrante.");
        }

        projetoUsuarioRepository.excluir(integranteId);
    }
}
