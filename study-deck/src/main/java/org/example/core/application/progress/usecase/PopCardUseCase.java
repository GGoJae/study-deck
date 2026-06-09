package org.example.core.application.progress.usecase;

import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.application.progress.vo.PopStrategy;

public interface PopCardUseCase {
    CardForDeck popNextCard(Long requesterId, Long subCategoryId, PopStrategy strategy);
}
