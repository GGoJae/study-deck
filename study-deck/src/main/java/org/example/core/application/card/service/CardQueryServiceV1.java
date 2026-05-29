package org.example.core.application.card.service;

import org.example.core.application.card.dto.response.AnswerCapture;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

import java.util.List;
import java.util.Optional;

public class CardQueryServiceV1 implements CardQueryUseCase {

    private final CardStore store;
    private final ToResponseMapper<Card, CardCapture> mapper;
    private final ToResponseMapper<Answer, AnswerCapture> answerMapper;

    public CardQueryServiceV1(CardStore cardStore, ToResponseMapper<Card, CardCapture> mapper, ToResponseMapper<Answer, AnswerCapture> answerMapper) {
        this.store = cardStore;
        this.mapper = mapper;
        this.answerMapper = answerMapper;
    }

    @Override
    public List<CardCapture> getOwnCardsForDisplay(Long requesterId, Long currentSubCategory) {
        return store.findBySubCategoryId(requesterId, currentSubCategory)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Optional<CardCapture> getCard(Long requesterId, Long cardId) {
        return store.findById(cardId).map(c -> {
            c.permissionCheck(requesterId);
            return mapper.toResponse(c);
        });

    }

    @Override
    public Optional<AnswerCapture> getBestAnswer(Long requesterId, Long cardId) {
        Card card = store.findById(cardId).orElseThrow();
        card.permissionCheck(requesterId);

        return store.findAnswerByAnswerId(card.getBestAnswer())
                .map(answerMapper::toResponse);
    }
}
