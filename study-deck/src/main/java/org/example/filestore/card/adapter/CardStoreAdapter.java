package org.example.filestore.card.adapter;

import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;
import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;

import java.io.IOException;

public class CardStoreAdapter implements CardStore {

    private final CardManager cardManager;
    private final FileSystemManager fileSystemManager;
    private final MetaDataManager metaDataManager;

    public CardStoreAdapter(CardManager cardManager, FileSystemManager fileSystemManager, MetaDataManager metaDataManager) {
        this.cardManager = cardManager;
        this.fileSystemManager = fileSystemManager;
        this.metaDataManager = metaDataManager;
    }

    @Override
    public Card save(Card card) {
        try {
            fileSystemManager.transaction();
            metaDataManager.transaction();
            cardManager.transaction();

            Long id = metaDataManager.nextCardId();
            Card withId = card.withId(id);
            CardModel jsonModel = CardModel.of(withId);

            cardManager.save(jsonModel);

            cardManager.commit();
            metaDataManager.commit();
            fileSystemManager.commit();

            return withId;
        } catch (IOException e) {
            cardManager.rollback();
            metaDataManager.rollback();
            fileSystemManager.rollback();
            throw new RuntimeException(e);
        }
    }
}
