package org.example.core.category.domain;

import java.time.Instant;
import java.util.Objects;


public class Category {
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        if (this.createdAt != null) throw new IllegalStateException();
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }

    public Long getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(Long updatedUser) {
        this.updatedUser = updatedUser;
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
