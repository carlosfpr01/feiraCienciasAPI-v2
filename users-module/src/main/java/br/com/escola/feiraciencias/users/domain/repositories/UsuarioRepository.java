package br.com.escola.feiraciencias.users.domain.repositories;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorEmail(String email);
    Page<Usuario> listarPorTipo(TipoUsuario tipo, int page, int size);
    Page<Usuario> listarAlunosPorProfessor(Integer professorId, int page, int size);
    void deletar(Integer id);
}
