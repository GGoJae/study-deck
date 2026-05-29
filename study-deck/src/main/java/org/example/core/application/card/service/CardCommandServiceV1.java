package org.example.core.application.card.service;

import org.example.core.application.card.dto.request.AddAnswer;
import org.example.core.application.card.dto.request.CreateCard;
import org.example.core.application.card.dto.request.DeleteCard;
import org.example.core.application.card.dto.request.UpdateBestAnswer;
import org.example.core.application.card.factory.AnswerFactory;
import org.example.core.application.card.factory.CardFactory;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

import java.util.Objects;

public class CardCommandServiceV1 implements CardCommandUseCase {

    public CardCommandServiceV1(CardStore store, CardFactory factory, AnswerFactory answerFactory) {
        this.store = store;
        this.cardFactory = factory;
        this.answerFactory = answerFactory;
    }

    private final CardStore store;
    private final CardFactory cardFactory;
    private final AnswerFactory answerFactory;

    @Override
    public Long create(CreateCard request) {
        Objects.requireNonNull(request);

        Card card = cardFactory.create(request.ownerId(), request.subCategoryId(),
                request.displayName(), request.question());
        Card saved = store.save(card);

        return saved.getId();
    }

    @Override
    public Long addAnswer(AddAnswer request) {
        Objects.requireNonNull(request);

        Long cardId = request.cardId();
        Card card = store.findById(cardId).orElseThrow();

        Long requesterId = request.requesterId();
        card.permissionCheck(requesterId);
        Answer answer = answerFactory.create(requesterId, cardId, request.answer());
        return store.addAnswer(cardId, answer);
    }

    @Override
    public Long delete(DeleteCard request) {
        Objects.requireNonNull(request);

        Card card = store.findById(request.cardId()).orElseThrow();
        Long requesterId = request.requesterId();

        card.permissionCheck(requesterId);

        store.delete(card);

        return card.getId();
    }

    @Override
    public void updateBestAnswer(UpdateBestAnswer request) {
        Objects.requireNonNull(request);

        Card card = store.findById(request.cardId()).orElseThrow();
        card.permissionCheck(request.requesterId());

        Card updated = card.changeBestAnswer(request.answerId());

        store.update(updated);
    }

}
