package org.example.core.application.card.factory;

import org.example.core.domain.card.Card;

import java.time.Instant;

public class CardFactoryV1 implements CardFactory{
    @Override
    public Card create(Long ownerId, Long subCategoryId, String displayName, String question) {
        Instant now = Instant.now();
        return new Card(null, ownerId, subCategoryId,
                displayName, question, null,
                now, now, ownerId, ownerId);
    }
}
