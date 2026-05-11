package org.example.core.application.category.dto.request;

public record CreateCategoryRequest(
        Long ownerId,
        String name,
        Integer sortKey
) {
}
