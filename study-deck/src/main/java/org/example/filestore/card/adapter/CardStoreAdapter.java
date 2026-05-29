package org.example.filestore.card.adapter;

import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;
import org.example.filestore.card.manager.AnswerManager;
import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.model.AnswerModel;
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
    private final AnswerManager answerManager;
    private final FileSystemManager fileSystemManager;
    private final MetaDataManager metaDataManager;
    private final ModelToDomainMapper<Card, CardModel> mapper;
    private final ModelToDomainMapper<Answer, AnswerModel> answerMapper;

    public CardStoreAdapter(CardManager cardManager, AnswerManager answerManager, FileSystemManager fileSystemManager, MetaDataManager metaDataManager, ModelToDomainMapper<Card, CardModel> mapper, ModelToDomainMapper<Answer, AnswerModel> answerMapper) {
        this.cardManager = cardManager;
        this.answerManager = answerManager;
        this.fileSystemManager = fileSystemManager;
        this.metaDataManager = metaDataManager;
        this.mapper = mapper;
        this.answerMapper = answerMapper;
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
            metaDataManager.transaction();
            answerManager.transaction();

            Long nextAnswerId = metaDataManager.nextAnswerId();
            Answer withId = answer.withId(nextAnswerId);
            AnswerModel answerModel = AnswerModel.of(withId);
            answerManager.save(answerModel);

            answerManager.commit();
            metaDataManager.commit();
            return nextAnswerId;

        } catch (IOException e) {
            answerManager.rollback();
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
    public void update(Card updated) {
        try {
            cardManager.transaction();

            CardModel cardModel = CardModel.of(updated);
            cardManager.update(cardModel);

            cardManager.commit();
        } catch (IOException e) {
            cardManager.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Answer> findAnswerByAnswerId(Long answerId) {
        try {
            return answerManager.findById(answerId)
                    .map(answerMapper::toDomain);
        } catch (IOException e) {
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
