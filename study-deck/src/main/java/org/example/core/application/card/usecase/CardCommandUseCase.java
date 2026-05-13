package org.example.core.application.card.usecase;

import org.example.core.application.card.dto.request.CreateCard;

public interface CardCommandUseCase {
    void create(CreateCard request);
}
