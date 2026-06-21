package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers.ProjetoPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProjetoPanacheRepository implements ProjetoRepository, PanacheRepositoryBase<ProjetoJpaEntity, Integer> {

    @Inject
    ProjetoPersistenceMapper mapper;

    @Override
    public Projeto salvar(Projeto projeto) {
        ProjetoJpaEntity entity = mapper.toEntity(projeto);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Projeto> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<Projeto> listarPorEvento(Integer eventoId) {
        return list("eventoId", eventoId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void excluir(Integer id) {
        deleteById(id);
    }
}
