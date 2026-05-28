package org.example.filestore.card.adapter;

import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;
import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.shared.model.Type;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CardStoreAdapter implements CardStore {

    private final CardManager cardManager;
    private final FileSystemManager fileSystemManager;
    private final MetaDataManager metaDataManager;
    private final ModelToDomainMapper<Card, CardModel> mapper;

    public CardStoreAdapter(CardManager cardManager, FileSystemManager fileSystemManager, MetaDataManager metaDataManager, ModelToDomainMapper<Card, CardModel> mapper) {
        this.cardManager = cardManager;
        this.fileSystemManager = fileSystemManager;
        this.metaDataManager = metaDataManager;
        this.mapper = mapper;
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

    @Override
    public Long addAnswer(Long cardId, Answer answer) {
        try {
            cardManager.transaction();
            metaDataManager.transaction();

            CardModel cardModel = cardManager.findById(cardId).orElseThrow();
            Long nextAnswerId = metaDataManager.nextAnswerId();
            Answer withId = answer.withId(nextAnswerId);
            CardModel updated = cardModel.addAnswer(withId);
            cardManager.update(updated);

            cardManager.commit();
            metaDataManager.commit();
            return nextAnswerId;

        } catch (IOException e) {
            cardManager.rollback();
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Card card) {
        try {
            cardManager.transaction();
            metaDataManager.transaction();

            cardManager.delete(card.getId());
            metaDataManager.ifCurrentContentReset(Type.CARD, card.getId());

            cardManager.commit();
            metaDataManager.commit();

        } catch (IOException e) {
            cardManager.rollback();
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> findBySubCategoryId(Long requesterId, Long subCategoryId) {
        try {
            return cardManager.findBySubCategoryId(subCategoryId)
                    .stream()
                    .filter(c -> Objects.equals(c.ownerId(), requesterId))
                    .map(mapper::toDomain)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Card> findById(Long cardId) {
        try {
            return cardManager.findById(cardId)
                    .map(mapper::toDomain);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
