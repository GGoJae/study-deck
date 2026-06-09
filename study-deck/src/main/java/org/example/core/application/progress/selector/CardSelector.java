package org.example.core.application.progress.selector;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.vo.PopStrategy;

public interface CardSelector {
    PopStrategy strategy();
    CardProgress pickNextCard(Deck deck);
}
