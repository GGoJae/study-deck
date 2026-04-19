package org.example.filestore.shared.model;

import java.time.Instant;

public record CategoryModel(
        Long id,
        String fileName,
        Long ownerId,
        String name,
        int sortKey,
        Instant createdAt,
        Instant updatedAt
) {
}
