package org.example.core.category.dto;

public record CategoryQuery(
        Long ownerId,
        int offSet,
        int limit
) {
}
