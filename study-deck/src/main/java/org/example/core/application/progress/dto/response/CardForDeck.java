package org.example.core.application.progress.dto.response;

public record CardForDeck(
        long cardId,
        String displayName,
        String question
) {
}
