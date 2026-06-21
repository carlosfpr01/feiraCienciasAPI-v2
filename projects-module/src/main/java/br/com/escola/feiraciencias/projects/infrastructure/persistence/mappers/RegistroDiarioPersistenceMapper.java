package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioArquivoJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioJpaEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.stream.Collectors;

@ApplicationScoped
public class RegistroDiarioPersistenceMapper {

    public RegistroDiario toDomain(RegistroDiarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        RegistroDiario domain = new RegistroDiario(
                entity.getId(),
                entity.getTexto(),
                entity.getDataCriacao(),
                entity.getCriadoPorId(),
                entity.getProjetoId()
        );

        if (entity.getArquivos() != null) {
            entity.getArquivos().forEach(arq -> domain.adicionarArquivoChave(arq.getArquivoChave()));
        }

        return domain;
    }

    public RegistroDiarioJpaEntity toEntity(RegistroDiario domain) {
        if (domain == null) {
            return null;
        }

        RegistroDiarioJpaEntity entity = new RegistroDiarioJpaEntity();
        entity.setId(domain.getId());
        entity.setTexto(domain.getTexto());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setCriadoPorId(domain.getCriadoPorId());
        entity.setProjetoId(domain.getProjetoId());

        if (domain.getArquivoChaves() != null) {
            entity.setArquivos(domain.getArquivoChaves().stream().map(chave -> {
                RegistroDiarioArquivoJpaEntity arq = new RegistroDiarioArquivoJpaEntity();
                arq.setArquivoChave(chave);
                arq.setRegistroDiario(entity);
                return arq;
            }).collect(Collectors.toList()));
        }

        return entity;
    }
}
