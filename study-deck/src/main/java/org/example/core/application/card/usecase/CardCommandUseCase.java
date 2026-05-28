package org.example.core.application.card.usecase;

import org.example.core.application.card.dto.request.AddAnswer;
import org.example.core.application.card.dto.request.CreateCard;
import org.example.core.application.card.dto.request.DeleteCard;

public interface CardCommandUseCase {
    Long create(CreateCard request);

    Long addAnswer(AddAnswer request);

    Long delete(DeleteCard request);
}
