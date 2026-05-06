package org.example.core.application.category.dto.request;

public record CategoryQuery(
        Long ownerId,
        int offSet,
        int limit
) {
}
