package org.example.filestore.card.mapper;

import org.example.core.domain.card.Card;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.shared.ModelToDomainMapper;

import java.util.List;

public class ToCardMapper implements ModelToDomainMapper<Card, CardModel> {
    @Override
    public Card toDomain(CardModel model) {
        return new Card(
                model.id(), model.ownerId(), model.subCategoryId(),
                model.displayName(), model.question(), model.bestAnswer(),
                model.createdAt(), model.updatedAt(),
                model.createdUser(), model.updatedUser()
        );
    }

    @Override
    public List<Card> toDomain(List<CardModel> models) {
        return models.stream()
                .map(this::toDomain)
                .toList();
    }
}
