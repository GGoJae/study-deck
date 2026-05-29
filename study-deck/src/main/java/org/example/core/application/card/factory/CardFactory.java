package org.example.core.application.card.factory;

import org.example.core.domain.card.Card;

public interface CardFactory {
    Card create(Long ownerId, Long subCategoryId, String displayName, String question);
}
