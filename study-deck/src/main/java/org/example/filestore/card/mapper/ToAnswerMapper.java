package org.example.filestore.card.mapper;

import org.example.core.domain.card.Answer;
import org.example.filestore.card.model.AnswerModel;
import org.example.filestore.shared.ModelToDomainMapper;

import java.util.List;

public class ToAnswerMapper implements ModelToDomainMapper<Answer, AnswerModel> {
    @Override
    public Answer toDomain(AnswerModel model) {
        return new Answer(
                model.id(), model.ownerId(), model.cardId(),
                model.content(), model.createdAt(), model.updatedAt(),
                model.createdUser(), model.updatedUser()
        );
    }

    @Override
    public List<Answer> toDomain(List<AnswerModel> models) {
        return models.stream().map(this::toDomain).toList();
    }
}
