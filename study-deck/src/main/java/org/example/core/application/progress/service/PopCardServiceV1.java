package org.example.core.application.progress.service;

import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.port.ProgressPort;
import org.example.core.application.progress.selector.CardSelector;
import org.example.core.application.progress.selector.SelectStrategyStorage;
import org.example.core.application.progress.usecase.PopCardUseCase;
import org.example.core.application.progress.vo.PopStrategy;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;

import java.util.List;
import java.util.Objects;

public class PopCardServiceV1 implements PopCardUseCase {

    private final CardStore cardStore;
    private final ProgressPort progressPort;
    private final SelectStrategyStorage selectStrategyStorage;
    private final ToResponseMapper<Card, CardForDeck> mapper;

    public PopCardServiceV1(CardStore cardStore, ProgressPort progressPort, SelectStrategyStorage selectStrategyStorage, ToResponseMapper<Card, CardForDeck> mapper) {
        this.cardStore = cardStore;
        this.progressPort = progressPort;
        this.selectStrategyStorage = selectStrategyStorage;
        this.mapper = mapper;
    }

    @Override
    public CardForDeck popNextCard(Long requesterId, Long subCategoryId, PopStrategy strategy) {

        List<Card> cards = cardStore.findBySubCategoryId(requesterId, subCategoryId);
        Deck deck = progressPort.getDeck(subCategoryId);
        Deck synchronizeWith = deck.synchronizeWith(cards);

        CardProgress nextProgress = selectStrategyStorage.pickNextCard(synchronizeWith, strategy);
        CardProgress choose = nextProgress.choose();

        Deck updated = synchronizeWith.progressUpdate(choose);
        progressPort.deckUpdate(updated);

        return cards.stream().filter(c -> Objects.equals(c.getId(), choose.cardId()))
                .findAny()
                .map(mapper::toResponse)
                .orElseThrow();
    }
}
