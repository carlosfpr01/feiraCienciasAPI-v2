package br.com.escola.feiraciencias.shared.infrastructure.api.mappers;

import br.com.escola.feiraciencias.shared.domain.exceptions.DomainException;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException exception) {
        // Fallback para outras exceções de domínio não mapeadas especificamente
        ErrorResponse error = ErrorResponse.of(exception.getMessage(), "DOMAIN_ERROR");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
