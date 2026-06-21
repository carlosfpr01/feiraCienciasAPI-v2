package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoUsuarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ProjetoUsuarioPersistenceMapper {
    ProjetoUsuario toDomain(ProjetoUsuarioJpaEntity entity);
    ProjetoUsuarioJpaEntity toEntity(ProjetoUsuario domain);
}
