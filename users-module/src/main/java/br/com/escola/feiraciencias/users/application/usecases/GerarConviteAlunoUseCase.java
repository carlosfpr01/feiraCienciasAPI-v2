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
public class GerarConviteAlunoUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Transactional
    public String execute(String nome, String matricula, String anoEscolar, Integer solicitanteId) {
        Usuario orientador = usuarioService.buscarPorIdOuFalhar(solicitanteId);

        if (!orientador.isProfessor()) {
            throw new BusinessRuleException("Apenas professores podem gerar convites para alunos.");
        }

        if (conviteRepository.existeConviteAtivoPorMatricula(matricula)) {
            throw new BusinessRuleException(
                    "Ja existe um convite ativo para a matricula: " + matricula);
        }

        ConviteRegistro convite = ConviteRegistro.criarParaAluno(nome, matricula, anoEscolar, solicitanteId);

        conviteRepository.salvar(convite);
        return convite.getToken();
    }
}
