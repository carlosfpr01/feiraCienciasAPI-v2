package br.com.escola.feiraciencias.projects.infrastructure.persistence.entities;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projetos")
@Getter
@Setter
@NoArgsConstructor
public class ProjetoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String materiais;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_apresentacao")
    private LocalDate dataApresentacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoProjeto situacao;

    @Column(name = "area_de_conhecimento", nullable = false, length = 100)
    private String areaDeConhecimento;

    @Column(nullable = false, length = 50)
    private String serie;

    @Column(name = "criado_por_id", nullable = false)
    private Integer criadoPorId;

    @Column(name = "evento_id", nullable = false)
    private Integer eventoId;

    @Column(name = "imagem_capa_chave", length = 500)
    private String imagemCapaChave;

    // Getters and Setters omitted
}
