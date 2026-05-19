package org.example.core.application.card.service;

import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardQueryUseCase;

import java.util.List;

public class CardQueryServiceV1 implements CardQueryUseCase {
    @Override
    public List<CardCapture> getOwnCardsForDisplay(Long requesterId, Long currentSubCategory) {
        // TODO subCategory 의 card 들 쿼리
        return List.of();
    }
}
