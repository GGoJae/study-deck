package org.example.core.application.card.dto.request;

public record AddAnswer(
        Long requesterId,
        Long cardId,
        String answer
) {
}
