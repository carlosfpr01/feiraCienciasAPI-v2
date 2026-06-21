package br.com.escola.feiraciencias.shared.infrastructure.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    String message,
    String code,
    LocalDateTime timestamp,
    List<String> details
) {
    public static ErrorResponse of(String message, String code) {
        return new ErrorResponse(message, code, LocalDateTime.now(), null);
    }

    public static ErrorResponse of(String message, String code, List<String> details) {
        return new ErrorResponse(message, code, LocalDateTime.now(), details);
    }
}
