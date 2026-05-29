package org.example.core.application.card.usecase;

import org.example.core.application.card.dto.response.AnswerCapture;
import org.example.core.application.card.dto.response.CardCapture;

import java.util.List;
import java.util.Optional;

public interface CardQueryUseCase {
    List<CardCapture> getOwnCardsForDisplay(Long requesterId, Long currentSubCategory);

    Optional<CardCapture> getCard(Long requesterId, Long cardId);

    Optional<AnswerCapture> getBestAnswer(Long requesterId, Long cardId);
}
