package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoUsuarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers.ProjetoUsuarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProjetoUsuarioPanacheRepository implements ProjetoUsuarioRepository, PanacheRepositoryBase<ProjetoUsuarioJpaEntity, Integer> {

    @Inject
    ProjetoUsuarioPersistenceMapper mapper;

    @Override
    public ProjetoUsuario salvar(ProjetoUsuario projetoUsuario) {
        ProjetoUsuarioJpaEntity entity = mapper.toEntity(projetoUsuario);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<ProjetoUsuario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<ProjetoUsuario> listarPorProjeto(Integer projetoId) {
        return list("projetoId", projetoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjetoUsuario> buscarPorProjetoEUsuario(Integer projetoId, Integer usuarioId) {
        return find("projetoId = ?1 and usuarioId = ?2", projetoId, usuarioId)
                .firstResultOptional()
                .map(mapper::toDomain);
    }

    @Override
    public void excluir(Integer id) {
        deleteById(id);
    }
}
