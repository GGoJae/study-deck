package org.example.core.application.progress.mapper;

import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.domain.card.Card;

public class ProgressToResponse implements ToResponseMapper<Card, CardForDeck> {
    @Override
    public CardForDeck toResponse(Card domain) {
        return new CardForDeck(domain.getId(), domain.getDisplayName(), domain.getQuestion());
    }
}
