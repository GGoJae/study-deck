package org.example.core.application.progress.service;

import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.port.ProgressPort;
import org.example.core.application.progress.selector.CardSelector;
import org.example.core.application.progress.usecase.PopCardUseCase;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

import java.util.List;

public class PopCardServiceV1 implements PopCardUseCase {

    private final CardStore cardStore;
    private final ProgressPort progressPort;
    private final CardSelector cardSelector;
    private final ToResponseMapper<CardProgress, CardForDeck> mapper;

    public PopCardServiceV1(CardStore cardStore, ProgressPort progressPort, CardSelector cardSelector, ToResponseMapper<CardProgress, CardForDeck> mapper) {
        this.cardStore = cardStore;
        this.progressPort = progressPort;
        this.cardSelector = cardSelector;
        this.mapper = mapper;
    }

    @Override
    public CardForDeck popNextCard(Long requesterId, Long subCategoryId) {

        List<Card> cards = cardStore.findBySubCategoryId(requesterId, subCategoryId);
        Deck deck = progressPort.getDeck(subCategoryId)
                .orElseGet(() -> Deck.init(subCategoryId));
        Deck synchronizeWith = deck.synchronizeWith(cards);

        CardProgress nextProgress = cardSelector.pickNextCard(synchronizeWith);
        CardProgress choose = nextProgress.choose();

        Deck updated = synchronizeWith.progressUpdate(choose);
        progressPort.deckUpdate(updated);

        return mapper.toResponse(nextProgress);
    }
}
