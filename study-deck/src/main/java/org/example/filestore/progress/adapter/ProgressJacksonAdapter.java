package org.example.filestore.progress.adapter;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.port.ProgressPort;
import org.example.filestore.progress.manager.ProgressManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProgressJacksonAdapter implements ProgressPort {

    private final ProgressManager progressManager;

    public ProgressJacksonAdapter(ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    @Override
    public Deck getDeck(Long subCategoryId) {
        try {
            List<CardProgress> progresses = progressManager.findBySubCategoryId(subCategoryId);
            return new Deck(subCategoryId, progresses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deckUpdate(Deck deck) {
        try {
            progressManager.transaction();

            progressManager.update(deck);

            progressManager.commit();
        } catch (IOException e) {
            progressManager.rollback();
            throw new RuntimeException(e);
        }
    }
}
