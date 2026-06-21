package br.com.escola.feiraciencias.projects.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {
    private Integer id;
    private String texto;
    private LocalDateTime dataComentario;
    private Integer criadoPorId;
    private Integer projetoId;

    // Getters and Setters omitted
}
