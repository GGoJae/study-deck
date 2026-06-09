package org.example.core.application.progress.selector;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.core.application.progress.vo.PopStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class OldestReviewedSelector implements CardSelector{

    @Override
    public PopStrategy strategy() {
        return PopStrategy.OLDEST;
    }

    @Override
    public CardProgress pickNextCard(Deck deck) {
        LocalDateTime now = LocalDateTime.now();
        return deck.progresses().stream()
                .max(Comparator.comparingLong(card -> this.calculateScore(card, now)))
                .orElseThrow(() -> new IllegalStateException("이 데크에는 풀 카드가 없습니다!"));
    }

    private long calculateScore(CardProgress card, LocalDateTime now) {
        if (Objects.isNull(card.lastReviewedAt())) return Long.MAX_VALUE;

        return Duration.between(card.lastReviewedAt(), now).toSeconds();
    }


}
