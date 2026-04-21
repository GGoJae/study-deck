package org.example.core.category.model;

public record CreateCategory(
        Long ownerId,
        String name,
        Integer sortKey
) {
}
