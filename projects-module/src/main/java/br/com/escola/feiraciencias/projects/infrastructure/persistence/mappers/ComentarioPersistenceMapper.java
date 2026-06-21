package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ComentarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ComentarioPersistenceMapper {
    @Mapping(source = "usuarioId", target = "criadoPorId")
    @Mapping(source = "dataCriacao", target = "dataComentario")
    Comentario toDomain(ComentarioJpaEntity entity);

    @Mapping(source = "criadoPorId", target = "usuarioId")
    @Mapping(source = "dataComentario", target = "dataCriacao")
    ComentarioJpaEntity toEntity(Comentario domain);
}
