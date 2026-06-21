package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GerarConviteProfessorUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Transactional
    public String execute(String nome, String disciplina, Integer solicitanteId) {
        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);

        if (!solicitante.isAdmin()) {
            throw new BusinessRuleException("Apenas administradores podem gerar convites para professores.");
        }

        ConviteRegistro convite = ConviteRegistro.criarParaProfessor(
            nome, 
            disciplina, 
            solicitanteId
        );

        conviteRepository.salvar(convite);
        return convite.getToken();
    }
}
