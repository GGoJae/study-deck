package org.example.filestore.card.model;

import org.example.core.domain.card.Answer;

import java.time.Instant;

public record AnswerModel(
        Long id,
        Long ownerId,
        Long cardId,
        String content,

        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {
    public static AnswerModel of(Answer answer) {
        return new AnswerModel(
                answer.getId(), answer.getOwnerId(), answer.getCardId(), answer.getContent(),
                answer.getCreatedAt(), answer.getUpdatedAt(),
                answer.getCreatedUser(), answer.getUpdatedUser()
        );
    }
}
