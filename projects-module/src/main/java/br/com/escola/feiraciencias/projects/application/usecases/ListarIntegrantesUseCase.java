package br.com.escola.feiraciencias.projects.application.usecases;

import java.util.List;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListarIntegrantesUseCase {

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    BuscarProjetoUseCase buscarProjetoUseCase;

    public List<ProjetoUsuario> execute(Integer projetoId) {
        buscarProjetoUseCase.execute(projetoId);
        return projetoUsuarioRepository.listarPorProjeto(projetoId);
    }
}
