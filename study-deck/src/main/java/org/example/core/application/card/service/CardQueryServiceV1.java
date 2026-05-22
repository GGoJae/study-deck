package org.example.core.application.card.service;

import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

import java.util.List;

public class CardQueryServiceV1 implements CardQueryUseCase {

    private final CardStore store;
    private final ToResponseMapper<Card, CardCapture> mapper;

    public CardQueryServiceV1(CardStore cardStore, ToResponseMapper<Card, CardCapture> mapper) {
        this.store = cardStore;
        this.mapper = mapper;
    }

    @Override
    public List<CardCapture> getOwnCardsForDisplay(Long requesterId, Long currentSubCategory) {
        return store.findBySubCategoryId(requesterId, currentSubCategory)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
