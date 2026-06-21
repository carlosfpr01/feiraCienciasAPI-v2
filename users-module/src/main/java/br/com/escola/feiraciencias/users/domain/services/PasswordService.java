package br.com.escola.feiraciencias.users.domain.services;

public interface PasswordService {
    /**
     * Gera um hash a partir de uma senha em texto plano.
     */
    String hash(String password);

    /**
     * Verifica se a senha em texto plano corresponde ao hash fornecido.
     */
    boolean verify(String password, String hashed);
}
