package br.com.escola.feiraciencias.users.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
public class ProfessorJpaEntity extends UsuarioJpaEntity {
    @Column(name = "is_adm", nullable = false)
    private Boolean isAdm;

    @Column(nullable = false, length = 120)
    private String materia;

    // Getters and Setters omitted
}
