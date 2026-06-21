package br.com.escola.feiraciencias.shared.domain.validation;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;

import java.util.regex.Pattern;

/**
 * Utilitário para validação de regras de domínio (Assertion Concern).
 * Garante que as entidades nunca fiquem em um estado inválido.
 */
public class DomainValidator {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static void notBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessRuleException(message);
        }
    }

    public static void notNull(Object value, String message) {
        if (value == null) {
            throw new BusinessRuleException(message);
        }
    }

    public static void minSize(String value, int min, String message) {
        if (value == null || value.trim().length() < min) {
            throw new BusinessRuleException(message);
        }
    }

    public static void maxSize(String value, int max, String message) {
        if (value != null && value.trim().length() > max) {
            throw new BusinessRuleException(message);
        }
    }

    public static void validEmail(String email, String message) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessRuleException(message);
        }
    }
}
