package br.com.escola.feiraciencias.projects.infrastructure.persistence.entities;

import br.com.escola.feiraciencias.shared.domain.enums.TipoIntegrante;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projeto_usuarios")
@Getter
@Setter
@NoArgsConstructor
public class ProjetoUsuarioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "projeto_id", nullable = false)
    private Integer projetoId;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_integrante", nullable = false)
    private TipoIntegrante tipoIntegrante;

    @Column(name = "data_vinculo", nullable = false)
    private LocalDateTime dataVinculo;
}
