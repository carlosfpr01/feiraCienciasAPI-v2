package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class Professor extends Usuario {
    private Boolean isAdm;
    private String materia;

    public Professor() {
        this.setTipoUsuario(TipoUsuario.PROFESSOR);
    }

    // Construtor completo para uso do MapStruct/Infra
    public Professor(Integer id, String nome, String email, String senha, TipoUsuario tipoUsuario, 
                     LocalDateTime dataCadastro, Integer criadoPorId, Boolean isAdm, String materia) {
        super(id, nome, email, senha, tipoUsuario, dataCadastro, criadoPorId);
        this.isAdm = isAdm;
        this.materia = materia;
    }
}
