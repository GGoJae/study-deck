package org.example.core.application.progress.selector;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.vo.PopStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectStrategyStorage {

    private final Map<PopStrategy, CardSelector> cardSelectorMap;

    public SelectStrategyStorage(List<CardSelector> cardSelectors) {
        this.cardSelectorMap = cardSelectors.stream().collect(
                Collectors.toMap(
                        CardSelector::strategy,
                        selector -> selector
                )
        );
    }

    public CardProgress pickNextCard(Deck deck, PopStrategy strategy) {
        return cardSelectorMap.get(strategy).pickNextCard(deck);
    }
}
