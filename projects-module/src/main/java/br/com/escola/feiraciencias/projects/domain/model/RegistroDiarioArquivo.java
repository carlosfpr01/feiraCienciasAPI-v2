package br.com.escola.feiraciencias.projects.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDiarioArquivo {
    private Integer id;
    private String arquivoId;       // UUID gerado pelo storage-module
    private String nomeOriginal;    // Cópia do nome para exibição sem consultar o storage
    private Integer registroDiarioId;
}
