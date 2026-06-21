package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ComentarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioJpaEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ProjetoPersistenceMapper {

    Projeto toDomain(ProjetoJpaEntity entity);
    ProjetoJpaEntity toEntity(Projeto domain);

}
