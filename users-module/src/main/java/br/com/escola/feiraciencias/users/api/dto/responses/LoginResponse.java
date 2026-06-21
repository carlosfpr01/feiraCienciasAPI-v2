package br.com.escola.feiraciencias.users.api.dto.responses;

public record LoginResponse(String token, UsuarioResponse usuario) {
}
