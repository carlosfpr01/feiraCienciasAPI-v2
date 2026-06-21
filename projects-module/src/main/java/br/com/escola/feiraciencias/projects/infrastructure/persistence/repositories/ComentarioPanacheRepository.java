package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.repositories.ComentarioRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ComentarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers.ComentarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ComentarioPanacheRepository implements ComentarioRepository, PanacheRepositoryBase<ComentarioJpaEntity, Integer> {

    @Inject
    ComentarioPersistenceMapper mapper;

    @Override
    public Comentario salvar(Comentario comentario) {
        ComentarioJpaEntity entity = mapper.toEntity(comentario);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Comentario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<Comentario> listarPorProjeto(Integer projetoId) {
        return list("projetoId", projetoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(Integer id) {
        deleteById(id);
    }
}
