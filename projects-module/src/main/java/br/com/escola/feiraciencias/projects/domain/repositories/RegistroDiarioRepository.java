package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import java.util.List;
import java.util.Optional;

public interface RegistroDiarioRepository {
    RegistroDiario salvar(RegistroDiario registro);
    Optional<RegistroDiario> buscarPorId(Integer id);
    List<RegistroDiario> listarPorProjeto(Integer projetoId);
}
