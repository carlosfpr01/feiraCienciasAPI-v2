package br.com.escola.feiraciencias.users.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.shared.domain.pagination.Page;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.UsuarioJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.mappers.UsuarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioPanacheRepository implements UsuarioRepository, PanacheRepositoryBase<UsuarioJpaEntity, Integer> {

    @Inject
    UsuarioPersistenceMapper persistenceMapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity entity = persistenceMapper.toEntity(usuario);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            entity = getEntityManager().merge(entity);
        }
        return persistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return find("email", email).firstResultOptional().map(persistenceMapper::toDomain);
    }

    @Override
    public Page<Usuario> listarPorTipo(TipoUsuario tipo, int page, int size) {
        var query = tipo != null
                ? find("tipoUsuario", tipo)
                : findAll();
        long total = query.count();
        List<Usuario> content = query.page(page, size).list()
                .stream().map(persistenceMapper::toDomain).toList();
        return new Page<>(content, total);
    }

    @Override
    public Page<Usuario> listarAlunosPorProfessor(Integer professorId, int page, int size) {
        var query = find("tipoUsuario = ?1 AND criadoPorId = ?2", TipoUsuario.ALUNO, professorId);
        long total = query.count();
        List<Usuario> content = query.page(page, size).list()
                .stream().map(persistenceMapper::toDomain).toList();
        return new Page<>(content, total);
    }

    @Override
    public void deletar(Integer id) {
        deleteById(id);
    }
}
