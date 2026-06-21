package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarMateriaisDescricaoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public Projeto execute(Integer id, String descricao, String materiais, Integer usuarioId) {
        Projeto projeto = buscarProjetoUseCase.execute(id);
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        boolean isCriador = projeto.getCriadoPorId().equals(usuarioId);
        boolean isAlunoIntegrante = false;

        if (!isCriador) {
            var vinculo = projetoUsuarioRepository.buscarPorProjetoEUsuario(id, usuarioId);
            if (vinculo.isPresent()) {
                isAlunoIntegrante = true;
            }
        }

        if (!isCriador && !isAlunoIntegrante) {
            throw new BusinessRuleException("Apenas o criador ou alunos integrantes do projeto podem atualizar materiais e descrição.");
        }

        projeto.setDescricao(descricao);
        projeto.setMateriais(materiais);
        return projetoRepository.salvar(projeto);
    }
}
