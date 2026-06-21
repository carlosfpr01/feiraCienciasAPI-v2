package br.com.escola.feiraciencias.users.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConviteRegistro {
    private Integer id;
    private String token;
    private String nome;
    private String matricula;
    private TipoUsuario tipoUsuario;
    private String vinculo; // Disciplina (Professor) ou Ano Escolar (Aluno)
    private Integer criadoPorId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;
    private StatusConvite status;

    public static ConviteRegistro criarParaAluno(String nome, String matricula, 
                                              String anoEscolar, Integer criadoPorId) {
        return ConviteRegistro.builder()
            .token(UUID.randomUUID().toString())
            .nome(nome)
            .matricula(matricula)
            .tipoUsuario(TipoUsuario.ALUNO)
            .vinculo(anoEscolar)
            .criadoPorId(criadoPorId)
            .dataCriacao(LocalDateTime.now())
            .dataExpiracao(LocalDateTime.now().plusDays(7))
            .status(StatusConvite.PENDENTE)
            .build();
    }

    public static ConviteRegistro criarParaProfessor(String nome, String disciplina, 
                                                  Integer criadoPorId) {
        return ConviteRegistro.builder()
                .token(UUID.randomUUID().toString())
                .nome(nome)
                .tipoUsuario(TipoUsuario.PROFESSOR)
                .vinculo(disciplina)
                .criadoPorId(criadoPorId)
                .dataCriacao(LocalDateTime.now())
                .dataExpiracao(LocalDateTime.now().plusDays(7))
                .status(StatusConvite.PENDENTE)
                .build();
    }

    public Usuario criarUsuario(String email) {
        return switch (tipoUsuario) {
            case ALUNO -> {
                Aluno aluno = Aluno.builder()
                        .nome(nome)
                        .email(email)
                        .tipoUsuario(TipoUsuario.ALUNO)
                        .matricula(matricula)
                        .anoEscolar(vinculo)
                        .build();
                aluno.vincularAoProfessor(criadoPorId);
                yield aluno;
            }
            case PROFESSOR -> Professor.builder()
                    .nome(nome)
                    .email(email)
                    .tipoUsuario(TipoUsuario.PROFESSOR)
                    .materia(vinculo)
                    .isAdm(false)
                    .build();
            
            default -> throw new BusinessRuleException(
                    "Tipo de convite inválido para registro: " + tipoUsuario);
        };
    }

    public void usar() {
        if (this.status != StatusConvite.PENDENTE) {
            throw new BusinessRuleException("Este convite não está mais pendente.");
        }
        if (LocalDateTime.now().isAfter(this.dataExpiracao)) {
            this.status = StatusConvite.EXPIRADO;
            throw new BusinessRuleException("Este convite já expirou.");
        }
        this.status = StatusConvite.USADO;
    }

    public void cancelar() {
        if (this.status != StatusConvite.PENDENTE) {
            throw new BusinessRuleException("Apenas convites pendentes podem ser cancelados.");
        }
        this.status = StatusConvite.CANCELADO;
    }
}
