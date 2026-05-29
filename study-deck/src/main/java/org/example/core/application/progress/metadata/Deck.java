package org.example.core.application.progress.metadata;

import org.example.core.domain.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Deck(
        long subCategoryId,
        List<CardProgress> progresses
) {

    public Deck {
        Objects.requireNonNull(progresses);
        progresses = new ArrayList<>(progresses);
    }

    public static Deck init(Long subCategoryId) {
        Objects.requireNonNull(subCategoryId);
        return new Deck(subCategoryId, new ArrayList<>());
    }

    public Deck synchronizeWith(List<Card> currentCards) {
        Set<Long> currentCardIds = currentCards.stream()
                .map(Card::getId)
                .collect(Collectors.toSet());

        List<CardProgress> newProgress = progresses.stream().filter(p -> currentCardIds.contains(p.cardId()))
                .collect(Collectors.toCollection(ArrayList::new));

        Set<Long> existingProgressCardIds = newProgress.stream()
                .map(CardProgress::cardId)
                .collect(Collectors.toSet());

        for (Card card : currentCards) {
            if (!existingProgressCardIds.contains(card.getId())) {
                newProgress.add(CardProgress.init(card.getId()));
            }
        }

        return new Deck(this.subCategoryId, newProgress);
    }

    public Deck progressUpdate(CardProgress progress) {
        List<CardProgress> updated = this.progresses.stream()
                .map(c -> Objects.equals(c.cardId(), progress.cardId()) ? progress : c)
                .toList();
        return new Deck(this.subCategoryId, updated);
    }
}
