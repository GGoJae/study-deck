package org.example.core.application.progress.metadata;

import java.time.LocalDateTime;

public record CardProgress(
        long cardId,
        LocalDateTime lastReviewedAt
) {

    public static CardProgress init(long cardId) {
        return new CardProgress(cardId, null);
    }

    public CardProgress choose() {
        return new CardProgress(this.cardId, LocalDateTime.now());
    }
}
