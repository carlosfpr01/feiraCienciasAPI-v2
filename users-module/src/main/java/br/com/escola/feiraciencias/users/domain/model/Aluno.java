package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.validation.DomainValidator;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class Aluno extends Usuario {
    private String matricula;
    private String anoEscolar;

    public Aluno() {
        this.setTipoUsuario(TipoUsuario.ALUNO);
    }

    // Construtor completo para uso do MapStruct/Infra
    public Aluno(Integer id, String nome, String email, String senha, TipoUsuario tipoUsuario, 
                 LocalDateTime dataCadastro, Integer criadoPorId, String matricula, String anoEscolar) {
        super(id, nome, email, senha, tipoUsuario, dataCadastro, criadoPorId);
        this.matricula = matricula;
        this.anoEscolar = anoEscolar;
    }

    /**
     * Define os dados escolares do aluno, garantindo que não sejam vazios.
     */
    public void definirDadosEscolares(String matricula, String anoEscolar) {
        DomainValidator.notBlank(matricula, "A matrícula do aluno é obrigatória.");
        DomainValidator.notBlank(anoEscolar, "O ano escolar do aluno é obrigatório.");
        
        this.matricula = matricula;
        this.anoEscolar = anoEscolar;
    }

    /**
     * Atualiza dados escolares parcialmente. Campos null são ignorados.
     */
    public void atualizarDadosEscolares(String matricula, String anoEscolar) {
        if (matricula != null) {
            DomainValidator.notBlank(matricula, "A matrícula não pode ser vazia.");
            this.matricula = matricula;
        }
        if (anoEscolar != null) {
            DomainValidator.notBlank(anoEscolar, "O ano escolar não pode ser vazio.");
            this.anoEscolar = anoEscolar;
        }
    }
}
