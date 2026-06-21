package br.com.escola.feiraciencias.users.infrastructure.jobs;

import br.com.escola.feiraciencias.users.domain.repositories.ConviteRegistroRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExpirarConvitesJob {

    private static final Logger LOG = Logger.getLogger(ExpirarConvitesJob.class);

    @Inject
    ConviteRegistroRepository conviteRepository;

    @Scheduled(every = "{feira-ciencias.jobs.expirar-convites.interval:1h}")
    @Transactional
    void expirarConvites() {
        LOG.info("Iniciando job de expiracao de convites...");
        try {
            int totalExpirados = conviteRepository.expirarConvitesAtrasados(LocalDateTime.now());
            if (totalExpirados > 0) {
                LOG.infof("Job concluido. %d convite(s) expirado(s) com sucesso.", totalExpirados);
            } else {
                LOG.info("Job concluido. Nenhum convite expirado.");
            }
        } catch (Exception e) {
            LOG.error("Erro ao executar job de expiracao de convites.", e);
        }
    }
}
