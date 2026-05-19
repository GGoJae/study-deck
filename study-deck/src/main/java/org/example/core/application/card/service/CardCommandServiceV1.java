package org.example.core.application.card.service;

import org.example.core.application.card.dto.request.CreateCard;
import org.example.core.application.card.factory.CardFactory;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

public class CardCommandServiceV1 implements CardCommandUseCase {

    public CardCommandServiceV1(CardStore store, CardFactory factory) {
        this.store = store;
        this.factory = factory;
    }

    private final CardStore store;
    private final CardFactory factory;

    @Override
    public Long create(CreateCard request) {
        Card card = factory.create(request.ownerId(), request.subCategoryId(),
                request.displayName(), request.question());
        Card saved = store.save(card);

        return saved.getId();
    }
}
