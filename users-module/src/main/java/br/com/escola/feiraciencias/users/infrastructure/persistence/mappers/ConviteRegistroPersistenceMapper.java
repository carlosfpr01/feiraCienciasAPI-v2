package br.com.escola.feiraciencias.users.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.ConviteRegistroJpaEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConviteRegistroPersistenceMapper {

    public ConviteRegistroJpaEntity toEntity(ConviteRegistro domain) {
        if (domain == null) return null;

        ConviteRegistroJpaEntity entity = new ConviteRegistroJpaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setNome(domain.getNome());
        entity.setMatricula(domain.getMatricula());
        entity.setTipoUsuario(domain.getTipoUsuario());
        entity.setVinculo(domain.getVinculo());
        entity.setCriadoPorId(domain.getCriadoPorId());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataExpiracao(domain.getDataExpiracao());
        entity.setStatus(domain.getStatus());
        
        return entity;
    }

    public ConviteRegistro toDomain(ConviteRegistroJpaEntity entity) {
        if (entity == null) return null;

        return ConviteRegistro.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .nome(entity.getNome())
                .matricula(entity.getMatricula())
                .tipoUsuario(entity.getTipoUsuario())
                .vinculo(entity.getVinculo())
                .criadoPorId(entity.getCriadoPorId())
                .dataCriacao(entity.getDataCriacao())
                .dataExpiracao(entity.getDataExpiracao())
                .status(entity.getStatus())
                .build();
    }
}
