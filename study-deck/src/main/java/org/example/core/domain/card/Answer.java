package org.example.core.domain.card;

import java.time.Instant;

public class Answer {

    private Long id;
    private Long ownerId;
    private Long cardId;
    private String content;

    private Instant createdAt;
    private Instant updatedAt;
    private Long createdUser;
    private Long updatedUser;

    public Answer(Long id, Long ownerId, Long cardId, String content, Instant createdAt, Instant updatedAt, Long createdUser, Long updatedUser) {
        this.id = id;
        this.ownerId = ownerId;
        this.cardId = cardId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdUser = createdUser;
        this.updatedUser = updatedUser;
    }



    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getCardId() {
        return cardId;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public Long getUpdatedUser() {
        return updatedUser;
    }

    public Answer withId(Long id) {
        return new Answer(
                id, this.ownerId, this.cardId,
                this.content, this.createdAt, this.updatedAt,
                this.createdUser, this.updatedUser
        );
    }
}
