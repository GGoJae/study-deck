package org.example.core.application.card.factory;

import org.example.core.domain.card.Answer;

import java.time.Instant;

public class AnswerFactoryV1 implements AnswerFactory{
    @Override
    public Answer create(Long requesterId, Long cardId, String answer) {
        Instant now = Instant.now();
        return new Answer(
                null, requesterId, cardId,
                answer, now, now,
                requesterId, requesterId
        );
    }
}
