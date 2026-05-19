package org.example.core.application.card.usecase;

import org.example.core.application.card.dto.request.CreateCard;

public interface CardCommandUseCase {
    Long create(CreateCard request);
}
