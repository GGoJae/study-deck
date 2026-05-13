package org.example.core.application.card.dto.request;

public record CreateCard(
        Long ownerId,
        Long subCategoryId,
        String displayName,
        String question
) {}
