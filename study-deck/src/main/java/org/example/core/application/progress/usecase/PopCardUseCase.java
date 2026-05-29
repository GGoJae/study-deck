package org.example.core.application.progress.usecase;

import org.example.core.application.progress.dto.response.CardForDeck;

public interface PopCardUseCase {
    CardForDeck popNextCard(Long requesterId, Long subCategoryId);
}
