package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers.RegistroDiarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistroDiarioPanacheRepository implements RegistroDiarioRepository, PanacheRepositoryBase<RegistroDiarioJpaEntity, Integer> {

    @Inject
    RegistroDiarioPersistenceMapper mapper;

    @Override
    public RegistroDiario salvar(RegistroDiario registro) {
        RegistroDiarioJpaEntity entity = mapper.toEntity(registro);
        
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<RegistroDiario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<RegistroDiario> listarPorProjeto(Integer projetoId) {
        return list("projetoId", projetoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
