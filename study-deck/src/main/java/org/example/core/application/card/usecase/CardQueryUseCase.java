package org.example.core.application.card.usecase;

import org.example.core.application.card.dto.response.CardCapture;

import java.util.List;

public interface CardQueryUseCase {
    List<CardCapture> getOwnCardsForDisplay(Long requesterId, Long currentSubCategory);
}
