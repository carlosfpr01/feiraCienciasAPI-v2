package br.com.escola.feiraciencias.users.api.dto.responses;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.model.Usuario;

public record UsuarioResponse(
        Integer id,
        String nome,
        String email,
        TipoUsuario tipoUsuario
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }
}
