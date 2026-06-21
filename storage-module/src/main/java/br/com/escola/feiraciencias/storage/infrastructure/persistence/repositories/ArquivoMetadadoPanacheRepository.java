package br.com.escola.feiraciencias.storage.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.storage.infrastructure.persistence.entities.ArquivoMetadadoJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArquivoMetadadoPanacheRepository implements PanacheRepositoryBase<ArquivoMetadadoJpaEntity, String> {

    /**
     * Remove metadados pelo valor da chave (key).
     */
    public long deleteByChave(String chave) {
        return delete("chave", chave);
    }
}
