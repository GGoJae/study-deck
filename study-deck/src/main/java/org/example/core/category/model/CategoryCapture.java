package org.example.core.category.model;

import java.time.Instant;

public record CategoryCapture(
        Long id,
        Long ownerId,
        String name,
        int sortKey,
        Instant createdAt,
        boolean isModified
) {
}
