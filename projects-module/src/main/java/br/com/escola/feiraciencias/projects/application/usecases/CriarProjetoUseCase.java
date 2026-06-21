package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CriarProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    br.com.escola.feiraciencias.events.application.services.EventoService eventoService;

    @Transactional
    public Projeto execute(Projeto projeto, Integer professorId) {
        var usuario = usuarioService.buscarUsuarioPorId(professorId);
        if (!usuario.isProfessor()) {
            throw new BusinessRuleException("Apenas professores podem criar projetos.");
        }

        // Verifica se o evento existe antes de criar o projeto
        eventoService.buscarPorIdOuFalhar(projeto.getEventoId());

        projeto.setDataCriacao(java.time.LocalDate.now());
        projeto.setCriadoPorId(professorId);
        projeto.setSituacao(SituacaoProjeto.ATIVO);

        return projetoRepository.salvar(projeto);
    }
}
