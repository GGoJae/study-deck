package org.example.core.application.category.dto.response;

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
