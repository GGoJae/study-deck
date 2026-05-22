package org.example.core.application.card.mapper;

import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.domain.card.Card;

public class CardToCaptureMapper implements ToResponseMapper<Card, CardCapture> {
    @Override
    public CardCapture toResponse(Card domain) {
        return new CardCapture(
                domain.getId(), domain.getOwnerId(), domain.getSubCategoryId(),
                domain.getDisplayName(), domain.getQuestion(), domain.getBestAnswer(),
                domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }
}
