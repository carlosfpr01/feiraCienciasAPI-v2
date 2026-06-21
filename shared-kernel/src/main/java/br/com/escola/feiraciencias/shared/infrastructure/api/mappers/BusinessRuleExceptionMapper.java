package br.com.escola.feiraciencias.shared.infrastructure.api.mappers;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.infrastructure.api.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessRuleExceptionMapper implements ExceptionMapper<BusinessRuleException> {
    @Override
    public Response toResponse(BusinessRuleException exception) {
        ErrorResponse error = ErrorResponse.of(exception.getMessage(), "BUSINESS_RULE_VIOLATION");
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
