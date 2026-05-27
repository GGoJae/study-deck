package org.example.core.application.progress.port;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;

import java.util.Optional;

public interface ProgressPort {

    Optional<Deck> getDeck(Long subCategoryId);
    void progressUpdate(CardProgress cardProgress);
    void deckUpdate(Deck deck);
}
