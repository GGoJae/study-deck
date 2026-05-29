package org.example.core.application.card.mapper;

import org.example.core.application.card.dto.response.AnswerCapture;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.domain.card.Answer;

public class AnswerToCaptureMapper implements ToResponseMapper<Answer, AnswerCapture> {
    @Override
    public AnswerCapture toResponse(Answer domain) {
        return new AnswerCapture(domain.getId(), domain.getCardId(), domain.getContent());
    }
}
