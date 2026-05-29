package org.example.core.domain.card;

import java.time.Instant;
import java.util.Objects;

public class Card {

    private Long id;
    private Long ownerId;
    private Long subCategoryId;
    private String displayName;
    private String question;
    private Long bestAnswer;
    private Instant createdAt;

    private Instant updatedAt;
    private Long createdUser;
    private Long updatedUser;

    public Card(Long id, Long ownerId, Long subCategoryId, String displayName, String question, Long bestAnswer, Instant createdAt, Instant updatedAt, Long createdUser, Long updatedUser) {
        this.id = id;
        this.ownerId = ownerId;
        this.subCategoryId = subCategoryId;
        this.displayName = displayName;
        this.question = question;
        this.bestAnswer = bestAnswer;
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

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getQuestion() {
        return question;
    }

    public Long getBestAnswer() {
        return bestAnswer;
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

    public Card withId(long id) {
        return new Card(
                id, this.ownerId, this.subCategoryId,
                this.displayName, this.question, this.bestAnswer,
                this.createdAt, this.updatedAt,
                this.createdUser, this.updatedUser
        );
    }

    public void permissionCheck(Long requesterId) {
        if (!Objects.equals(this.ownerId, requesterId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    public Card changeBestAnswer(Long answerId) {
        return new Card(
                this.id, this.ownerId, this.subCategoryId,
                this.displayName, this.question, answerId,
                this.createdAt, this.updatedAt,
                this.createdUser, this.updatedUser
        );
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Card card = (Card) object;
        return Objects.equals(id, card.id) && Objects.equals(ownerId, card.ownerId) && Objects.equals(subCategoryId, card.subCategoryId) && Objects.equals(displayName, card.displayName) && Objects.equals(question, card.question) && Objects.equals(bestAnswer, card.bestAnswer) && Objects.equals(createdAt, card.createdAt) && Objects.equals(updatedAt, card.updatedAt) && Objects.equals(createdUser, card.createdUser) && Objects.equals(updatedUser, card.updatedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, subCategoryId, displayName, question, bestAnswer, createdAt, updatedAt, createdUser, updatedUser);
    }

}
