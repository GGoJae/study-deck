package org.example.core.domain.card;

import java.util.List;

public interface CardStore {
    Card save(Card card);

    List<Card> findBySubCategoryId(Long requesterId, Long subCategoryId);
}
