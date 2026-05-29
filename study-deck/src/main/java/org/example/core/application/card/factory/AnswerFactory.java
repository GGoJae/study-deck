package org.example.core.application.card.factory;

import org.example.core.domain.card.Answer;

public interface AnswerFactory {
    Answer create(Long requesterId, Long cardId, String answer);
}
