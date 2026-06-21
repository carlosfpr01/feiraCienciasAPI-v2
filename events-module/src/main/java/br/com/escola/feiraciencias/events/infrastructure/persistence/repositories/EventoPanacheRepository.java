package br.com.escola.feiraciencias.events.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.events.infrastructure.persistence.entities.EventoJpaEntity;
import br.com.escola.feiraciencias.events.infrastructure.persistence.mappers.EventoPersistenceMapper;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventoPanacheRepository implements EventoRepository, PanacheRepositoryBase<EventoJpaEntity, Integer> {

    @Inject
    EventoPersistenceMapper mapper;

    @Override
    public Evento salvar(Evento evento) {
        EventoJpaEntity entity = mapper.toEntity(evento);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Evento> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<Evento> listarTodos() {
        return listAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<Evento> listarPaginado(int page, int size) {
        var query = findAll();
        long total = query.count();
        List<Evento> content = query.page(page, size).list()
                .stream().map(mapper::toDomain).collect(Collectors.toList());
        return new Page<>(content, total);
    }

    @Override
    public void excluir(Integer id) {
        deleteById(id);
        // Impl delete
    }
}
