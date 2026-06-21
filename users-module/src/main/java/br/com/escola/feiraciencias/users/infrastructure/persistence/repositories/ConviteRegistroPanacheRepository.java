package br.com.escola.feiraciencias.users.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.ConviteRegistroJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.mappers.ConviteRegistroPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ConviteRegistroPanacheRepository implements ConviteRegistroRepository, PanacheRepositoryBase<ConviteRegistroJpaEntity, Integer> {

    @Inject
    ConviteRegistroPersistenceMapper mapper;

    @Override
    public ConviteRegistro salvar(ConviteRegistro convite) {
        ConviteRegistroJpaEntity entity = mapper.toEntity(convite);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<ConviteRegistro> buscarPorToken(String token) {
        return find("token", token).firstResultOptional().map(mapper::toDomain);
    }

    @Override
    public Optional<ConviteRegistro> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public Page<ConviteRegistro> listarPorCriador(Integer criadorId, StatusConvite status, int page, int size) {
        PanacheQuery<ConviteRegistroJpaEntity> query = buildQuery(criadorId, status);
        long total = query.count();
        List<ConviteRegistro> content = query.page(page, size).list()
                .stream().map(mapper::toDomain).toList();
        return new Page<>(content, total);
    }

    private PanacheQuery<ConviteRegistroJpaEntity> buildQuery(Integer criadorId, StatusConvite status) {
        if (criadorId != null && status != null) {
            return find("criadoPorId = ?1 AND status = ?2", criadorId, status);
        } else if (criadorId != null) {
            return find("criadoPorId", criadorId);
        } else if (status != null) {
            return find("status", status);
        } else {
            return findAll();
        }
    }

    @Override
    public boolean existeConviteAtivoPorMatricula(String matricula) {
        return count("matricula = ?1 AND status = ?2 AND dataExpiracao > ?3",
                matricula, StatusConvite.PENDENTE, LocalDateTime.now()) > 0;
    }

    @Override
    public int expirarConvitesAtrasados(LocalDateTime agora) {
        return update("status = ?1 where status = ?2 and dataExpiracao < ?3",
                StatusConvite.EXPIRADO, StatusConvite.PENDENTE, agora);
    }
}
