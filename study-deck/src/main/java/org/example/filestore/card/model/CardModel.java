package org.example.filestore.card.model;

import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record CardModel(
    Long id,
    Long ownerId,
    Long subCategoryId,
    String displayName,
    String question,
    Long bestAnswer,
    List<AnswerModel> answers,

    Instant createdAt,
    Instant updatedAt,
    Long createdUser,
    Long updatedUser
) {
    public static CardModel of(Card domain) {
        return new CardModel(
                domain.getId(), domain.getOwnerId(), domain.getSubCategoryId(), domain.getDisplayName(),
                domain.getQuestion(), domain.getBestAnswer(), List.of(),
                domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }

    public List<AnswerModel> answers() {
        return List.copyOf(this.answers);
    }

    public CardModel addAnswer(Answer answer) {
        Objects.requireNonNull(answer);
        if (!Objects.equals(this.ownerId, answer.getOwnerId())) throw new IllegalStateException();
        if (!Objects.equals(this.id, answer.getCardId())) throw new IllegalStateException();

        List<AnswerModel> newList = new java.util.ArrayList<>(this.answers);
        newList.add(AnswerModel.of(answer));

        return new CardModel(
                this.id, this.ownerId, this.subCategoryId,
                this.displayName, this.question, this.bestAnswer,
                newList, this.createdAt, this.updatedAt,
                this.createdUser, this.updatedUser
        );
    }

    public record AnswerModel(
            Long id,
            String content,

            Instant createdAt,
            Instant updatedAt,
            Long createdUser,
            Long updatedUser
    ) {
        public static AnswerModel of(Answer answer) {
            return new AnswerModel(
                    answer.getId(), answer.getContent(),
                    answer.getCreatedAt(), answer.getUpdatedAt(),
                    answer.getCreatedUser(), answer.getUpdatedUser()
            );
        }
    }
}
