package br.com.escola.feiraciencias.users.api.dto.requests;

public record AtualizarUsuarioRequest(
        String nome,
        String email,
        String matricula,
        String anoEscolar,
        String materia
) {}
