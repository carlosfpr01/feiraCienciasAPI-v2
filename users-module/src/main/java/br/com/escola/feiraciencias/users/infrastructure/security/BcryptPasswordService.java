package br.com.escola.feiraciencias.users.infrastructure.security;

import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BcryptPasswordService implements PasswordService {

    @Override
    public String hash(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    @Override
    public boolean verify(String password, String hashed) {
        return BcryptUtil.matches(password, hashed);
    }
}
