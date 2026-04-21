package org.example.filestore.category.model;

import java.time.Instant;

public record CategoryModel(
        Long id,
        String fileName,
        Long ownerId,
        String name,
        int sortKey,
        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {
}
