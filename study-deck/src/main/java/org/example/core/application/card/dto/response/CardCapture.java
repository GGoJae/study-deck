package org.example.core.application.card.dto.response;

import java.time.Instant;

public record CardCapture(
        Long id,
        Long ownerId,
        Long subCategoryId,
        String displayName,
        String question,
        Long bestAnswer,
        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {
}
