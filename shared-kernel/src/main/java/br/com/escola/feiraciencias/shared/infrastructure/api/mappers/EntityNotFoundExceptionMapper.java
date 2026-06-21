package br.com.escola.feiraciencias.shared.infrastructure.api.mappers;

import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
    @Override
    public Response toResponse(EntityNotFoundException exception) {
        ErrorResponse error = ErrorResponse.of(exception.getMessage(), "ENTITY_NOT_FOUND");
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
