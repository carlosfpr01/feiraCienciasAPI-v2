package br.com.escola.feiraciencias.users.infrastructure.persistence.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.AlunoJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.ProfessorJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.UsuarioJpaEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsuarioPersistenceMapper {

    @SubclassMapping(source = AlunoJpaEntity.class, target = Aluno.class)
    @SubclassMapping(source = ProfessorJpaEntity.class, target = Professor.class)
    @BeanMapping(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
    Usuario toDomain(UsuarioJpaEntity entity);

    @SubclassMapping(source = Aluno.class, target = AlunoJpaEntity.class)
    @SubclassMapping(source = Professor.class, target = ProfessorJpaEntity.class)
    @BeanMapping(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
    UsuarioJpaEntity toEntity(Usuario domain);

    // Métodos de suporte para as subclasses (evitam ambiguidade por terem nomes diferentes)
    Aluno toAlunoDomain(AlunoJpaEntity entity);
    Professor toProfessorDomain(ProfessorJpaEntity entity);
    
    AlunoJpaEntity toAlunoEntity(Aluno domain);
    ProfessorJpaEntity toProfessorEntity(Professor domain);
}
