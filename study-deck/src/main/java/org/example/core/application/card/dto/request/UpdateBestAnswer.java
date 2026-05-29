package org.example.core.application.card.dto.request;

public record UpdateBestAnswer(
        Long requesterId,
        Long cardId,
        Long answerId
) {
}
