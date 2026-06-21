package br.com.escola.feiraciencias.projects.application.usecases;

import java.time.LocalDateTime;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.TipoIntegrante;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdicionarIntegranteUseCase {

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public ProjetoUsuario execute(Integer projetoId, Integer usuarioId, String tipoIntegrante, Integer professorId) {
        var projeto = buscarProjetoUseCase.execute(projetoId);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o professor criador pode adicionar integrantes.");
        }

        // Validar usuário
        usuarioService.buscarUsuarioPorId(usuarioId);

        ProjetoUsuario integrante = new ProjetoUsuario();
        integrante.setProjetoId(projetoId);
        integrante.setUsuarioId(usuarioId);
        integrante.setTipoIntegrante(TipoIntegrante.valueOf(tipoIntegrante));
        integrante.setDataVinculo(LocalDateTime.now());

        return projetoUsuarioRepository.salvar(integrante);
    }
}
