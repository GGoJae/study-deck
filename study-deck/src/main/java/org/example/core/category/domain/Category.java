package org.example.core.category.domain;

import java.time.Instant;
import java.util.Objects;


public class Category {

    public Category(Long id, Long ownerId, String name, int sortKey, Instant createdAt, Instant updatedAt, Long createdUser, Long updatedUser) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.sortKey = sortKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdUser = createdUser;
        this.updatedUser = updatedUser;
    }

    private Long id;
    private Long ownerId;
    private String name;
    private int sortKey;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdUser;
    private Long updatedUser;

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public int getSortKey() {
        return sortKey;
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

    public Category withId(long id) {
        return new Category(id, this.ownerId, this.name, this.sortKey,
                this.createdAt, this.updatedAt, this.createdUser, this.updatedUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getSortKey() == category.getSortKey() && Objects.equals(getId(), category.getId()) && Objects.equals(getOwnerId(), category.getOwnerId()) && Objects.equals(getName(), category.getName()) && Objects.equals(getCreatedAt(), category.getCreatedAt()) && Objects.equals(getUpdatedAt(), category.getUpdatedAt()) && Objects.equals(getCreatedUser(), category.getCreatedUser()) && Objects.equals(getUpdatedUser(), category.getUpdatedUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId(), getName(), getSortKey(), getCreatedAt(), getUpdatedAt(), getCreatedUser(), getUpdatedUser());
    }

}
