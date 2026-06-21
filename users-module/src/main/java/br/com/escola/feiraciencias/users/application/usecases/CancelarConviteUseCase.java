package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CancelarConviteUseCase {

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public void execute(Integer solicitanteId, Integer conviteId) {
        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        ConviteRegistro convite = conviteRepository.buscarPorId(conviteId)
                .orElseThrow(() -> new EntityNotFoundException("Convite nao encontrado."));

        if (solicitante.getTipoUsuario() == TipoUsuario.PROFESSOR
                && !solicitanteId.equals(convite.getCriadoPorId())) {
            throw new BusinessRuleException(
                    "Professores so podem cancelar convites criados por eles.");
        }

        convite.cancelar();
        conviteRepository.salvar(convite);
    }
}
