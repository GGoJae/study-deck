package org.example.filestore.card.model;

import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record CardModel(
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
    public static CardModel of(Card domain) {
        return new CardModel(
                domain.getId(), domain.getOwnerId(), domain.getSubCategoryId(), domain.getDisplayName(),
                domain.getQuestion(), domain.getBestAnswer(),
                domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }

    public CardModel updateCard(CardModel card) {
        Objects.requireNonNull(card);
        if (!Objects.equals(this.ownerId, card.ownerId())) throw new IllegalStateException();
        if (!Objects.equals(card.id, this.id)) {
            throw new IllegalStateException();
        }

        return new CardModel(
                card.id, card.ownerId, card.subCategoryId,
                card.displayName, card.question, card.bestAnswer,
                card.createdAt, card.updatedAt,
                card.createdUser, card.updatedUser
        );
    }

}
