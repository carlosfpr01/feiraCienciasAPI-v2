package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import java.util.List;
import java.util.Optional;

public interface ProjetoUsuarioRepository {
    ProjetoUsuario salvar(ProjetoUsuario projetoUsuario);
    Optional<ProjetoUsuario> buscarPorId(Integer id);
    List<ProjetoUsuario> listarPorProjeto(Integer projetoId);
    Optional<ProjetoUsuario> buscarPorProjetoEUsuario(Integer projetoId, Integer usuarioId);
    void excluir(Integer id);
}
