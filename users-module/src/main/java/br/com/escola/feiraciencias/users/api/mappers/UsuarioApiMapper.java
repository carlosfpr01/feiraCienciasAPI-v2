package br.com.escola.feiraciencias.users.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsuarioApiMapper {
    // Mapeamentos de DTO removidos pois a logica de criacao agora usa ConviteRegistro
}
