package org.example.core.application.card.dto.request;

public record DeleteCard(
        Long requesterId,
        Long cardId
) {
}
