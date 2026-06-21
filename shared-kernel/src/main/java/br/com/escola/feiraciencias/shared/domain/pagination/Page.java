package br.com.escola.feiraciencias.shared.domain.pagination;

import java.util.List;

public record Page<T>(List<T> content, long total) {}
