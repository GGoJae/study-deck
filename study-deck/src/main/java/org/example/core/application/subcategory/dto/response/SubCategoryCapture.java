package org.example.core.application.subcategory.dto.response;

import java.time.Instant;

public record SubCategoryCapture(
        Long id,
        Long ownerId,
        Long categoryId,
        String name,
        int sortKey,
        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {
}
