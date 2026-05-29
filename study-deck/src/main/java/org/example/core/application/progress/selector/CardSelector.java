package org.example.core.application.progress.selector;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;

public interface CardSelector {
    CardProgress pickNextCard(Deck deck);
}
