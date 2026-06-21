package br.com.escola.feiraciencias.users.infrastructure.persistence;

import java.time.LocalDateTime;

import org.jboss.logging.Logger;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@IfBuildProfile("dev")
public class DevDataLoader {

    private static final Logger LOGGER = Logger.getLogger(DevDataLoader.class);

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        String email = "professor@escola.com";
        
        if (usuarioRepository.buscarPorEmail(email).isEmpty()) {
            LOGGER.info("Criando professor de teste padrão...");

            String senhaHasheada = passwordService.hash("123456");

            Professor professor = Professor.builder()
                    .nome("Professor Global")
                    .email(email)
                    .senha(senhaHasheada)
                    .tipoUsuario(TipoUsuario.ADMIN)
                    .dataCadastro(LocalDateTime.now())
                    .isAdm(true)
                    .materia("Ciências Naturais")
                    .build();

            usuarioRepository.salvar(professor);
            LOGGER.info("Professor de teste criado com sucesso! Senha: 123456");
        }
    }
}
