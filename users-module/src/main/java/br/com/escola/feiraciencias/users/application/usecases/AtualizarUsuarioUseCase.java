package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizarUsuarioUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario execute(Integer solicitanteId, Integer targetId,
                           String nome, String email, String matricula, String anoEscolar, String materia) {

        Usuario solicitante = usuarioService.buscarPorIdOuFalhar(solicitanteId);
        Usuario target = usuarioService.buscarPorIdOuFalhar(targetId);

        validarPermissao(solicitanteId, solicitante.getTipoUsuario(), target);

        if (solicitante.getTipoUsuario() == TipoUsuario.ALUNO) {
            // ALUNO so atualiza o proprio email
            target.atualizarContato(null, email);
        } else if (solicitante.getTipoUsuario() == TipoUsuario.PROFESSOR) {
            boolean editandoASiMesmo = solicitanteId.equals(targetId);
            if (editandoASiMesmo) {
                target.atualizarContato(null, email);
            } else {
                // Editando aluno vinculado: pode alterar nome, email, dados escolares
                target.atualizarContato(nome, email);
                if (target instanceof Aluno aluno) {
                    aluno.atualizarDadosEscolares(matricula, anoEscolar);
                }
            }
        } else {
            // ADMIN: atualiza tudo exceto senha
            target.atualizarContato(nome, email);
            if (target instanceof Aluno aluno) {
                aluno.atualizarDadosEscolares(matricula, anoEscolar);
            }
        }

        return usuarioRepository.salvar(target);
    }

    private void validarPermissao(Integer solicitanteId, TipoUsuario solicitanteRole, Usuario target) {
        switch (solicitanteRole) {
            case ALUNO -> {
                if (!solicitanteId.equals(target.getId())) {
                    throw new BusinessRuleException("Alunos so podem editar os proprios dados.");
                }
            }
            case PROFESSOR -> {
                boolean editandoASiMesmo = solicitanteId.equals(target.getId());
                boolean editandoAlunoVinculado = target.isAluno()
                        && solicitanteId.equals(target.getCriadoPorId());
                if (!editandoASiMesmo && !editandoAlunoVinculado) {
                    throw new BusinessRuleException(
                            "Professores so podem editar os proprios dados ou alunos vinculados.");
                }
            }
            case ADMIN -> {
                if (target.isAdmin()) {
                    throw new BusinessRuleException("Administradores nao podem modificar outros administradores.");
                }
            }
        }
    }
}
