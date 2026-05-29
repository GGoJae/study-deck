package org.example.core.domain.card;

import java.util.List;
import java.util.Optional;

public interface CardStore {
    Card save(Card card);

    List<Card> findBySubCategoryId(Long requesterId, Long subCategoryId);

    Optional<Card> findById(Long cardId);

    Long addAnswer(Long cardId, Answer answer);

    void delete(Card card);

    void update(Card updated);

    Optional<Answer> findAnswerByAnswerId(Long answerId);
}
