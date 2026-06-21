package br.com.escola.feiraciencias.shared.infrastructure.api.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long total,
        int totalPages,
        boolean hasMore
) {
    public PageResponse(List<T> content, int page, int size, long total) {
        this(
            content,
            page,
            size,
            total,
            size > 0 ? (int) Math.ceil((double) total / size) : 0,
            (long) (page + 1) * size < total
        );
    }
}

