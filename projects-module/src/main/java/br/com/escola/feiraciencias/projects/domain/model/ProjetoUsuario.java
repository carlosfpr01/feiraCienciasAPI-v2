package br.com.escola.feiraciencias.projects.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoIntegrante;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoUsuario {
    private Integer id;
    private Integer projetoId;
    private Integer usuarioId;
    private TipoIntegrante tipoIntegrante;
    private LocalDateTime dataVinculo;

    // Getters and Setters omitted
}
