package br.com.escola.feiraciencias.users.api.dto.responses;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.users.domain.enums.StatusConvite;
import br.com.escola.feiraciencias.users.domain.model.ConviteRegistro;

import java.time.LocalDateTime;

public record ConviteResponse(
        Integer id,
        String nome,
        String matricula,
        TipoUsuario tipoUsuario,
        String vinculo,
        StatusConvite status,
        LocalDateTime dataExpiracao
) {
    public static ConviteResponse from(ConviteRegistro convite) {
        return new ConviteResponse(
                convite.getId(),
                convite.getNome(),
                convite.getMatricula(),
                convite.getTipoUsuario(),
                convite.getVinculo(),
                convite.getStatus(),
                convite.getDataExpiracao()
        );
    }
}
