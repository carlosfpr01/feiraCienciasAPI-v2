package br.com.escola.feiraciencias.users.domain.model;

import java.time.LocalDateTime;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.validation.DomainValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// @Setter REMOVIDO: O domínio não expõe mutação arbitrária de estado.
// Use os métodos de negócio abaixo para alterar o estado de forma controlada.
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;
    private LocalDateTime dataCadastro;
    private Integer criadoPorId;

    /**
     * Registra o usuário no sistema, definindo a senha hasheada e a data de cadastro.
     * Deve ser chamado apenas uma vez, no momento do cadastro.
     */
    public void registrar(String senhaHasheada) {
        DomainValidator.notBlank(this.nome, "O nome do usuário é obrigatório.");
        DomainValidator.validEmail(this.email, "O email informado é inválido.");
        DomainValidator.notBlank(senhaHasheada, "A senha não pode ser vazia.");

        this.senha = senhaHasheada;
        this.dataCadastro = LocalDateTime.now();
    }

    /**
     * Vincula este usuário (Aluno) ao professor que o cadastrou.
     */
    public void vincularAoProfessor(Integer professorId) {
        this.criadoPorId = professorId;
    }

    // Setter de tipoUsuario mantido como protected para uso apenas por subclasses
    protected void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

        public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(tipoUsuario);
    }

    public boolean isProfessor() {
        return TipoUsuario.PROFESSOR.equals(tipoUsuario) || TipoUsuario.ADMIN.equals(tipoUsuario);
    }
 
    public boolean isAluno() {
        return TipoUsuario.ALUNO.equals(tipoUsuario);
    }

    /**
     * Atualiza nome e/ou email do usuario.
     * Nome pode ser null -- nesse caso nao e alterado.
     */
    public void atualizarContato(String nome, String email) {
        if (nome != null) {
            DomainValidator.notBlank(nome, "O nome nao pode ser vazio.");
            this.nome = nome;
        }
        if (email != null) {
            DomainValidator.validEmail(email, "O email informado e invalido.");
            this.email = email;
        }
    }

    /**
     * Troca a senha do proprio usuario, exigindo confirmacao da senha atual.
     */
    public void alterarSenha(String senhaAtualHash, String novaSenhaHash) {
        if (!this.senha.equals(senhaAtualHash)) {
            throw new BusinessRuleException("Senha atual incorreta.");
        }
        DomainValidator.notBlank(novaSenhaHash, "A nova senha nao pode ser vazia.");
        this.senha = novaSenhaHash;
    }

    /**
     * Reseta a senha do usuario sem exigir a senha atual.
     * Uso exclusivo por PROFESSOR (aluno vinculado) ou ADMIN.
     */
    public void resetarSenha(String novaSenhaHash) {
        DomainValidator.notBlank(novaSenhaHash, "A nova senha nao pode ser vazia.");
        this.senha = novaSenhaHash;
    }
}
