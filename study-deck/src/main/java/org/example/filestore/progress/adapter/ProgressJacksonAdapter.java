package org.example.filestore.progress.adapter;

import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.port.ProgressPort;
import org.example.filestore.progress.manager.ProgressManager;

import java.io.IOException;
import java.util.Optional;

public class ProgressJacksonAdapter implements ProgressPort {

    private final ProgressManager progressManager;

    public ProgressJacksonAdapter(ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    @Override
    public Optional<Deck> getDeck(Long subCategoryId) {
        try {
            return progressManager.findBySubCategoryId(subCategoryId);
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
