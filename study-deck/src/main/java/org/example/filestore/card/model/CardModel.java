package org.example.filestore.card.model;

import org.example.core.domain.card.Card;

import java.time.Instant;
import java.util.List;

public record CardModel(
    Long id,
    Long ownerId,
    Long subCategoryId,
    String displayName,
    String question,
    Long bestAnswer,
    List<AnswerModel> answers,

    Instant createdAt,
    Instant updatedAt,
    Long createdUser,
    Long updatedUser
) {
    public static CardModel of(Card domain) {
        return new CardModel(
                domain.getId(), domain.getOwnerId(), domain.getSubCategoryId(), domain.getDisplayName(),
                domain.getQuestion(), domain.getBestAnswer(), List.of(),
                domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }

    public record AnswerModel(
            Long id,
            String content,

            Instant createdAt,
            Instant updatedAt,
            Long createdUser,
            Long updatedUser
    ) {

    }
}
