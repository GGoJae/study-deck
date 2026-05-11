package org.example.core.domain.category;

import java.time.Instant;
import java.util.Objects;


public class Category {

    public Category(Long id, Long ownerId, String name, int sortKey, Instant createdAt, Instant updatedAt, Long createdUser, Long updatedUser) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId는 필수입니다.");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("생성 일자는 필수입니다.");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("수정 일자는 필수입니다.");
        }
        if (createdUser == null) {
            throw new IllegalArgumentException("생성자 id는 필수입니다.");
        }
        if (updatedUser == null) {
            throw new IllegalArgumentException("수정자 id는 필수입니다.");
        }

        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.sortKey = sortKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdUser = createdUser;
        this.updatedUser = updatedUser;
    }

    private final Long id;
    private final Long ownerId;
    private final String name;
    private final int sortKey;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Long createdUser;
    private final Long updatedUser;

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

    public Category rename(String name, Long requesterId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (requesterId == null) {
            throw new IllegalArgumentException("수정자 id는 필수입니다.");
        }
        permissionCheck(requesterId);
        Instant now = Instant.now();

        return new Category(
                this.id, this.ownerId, name, this.sortKey,
                this.createdAt, now, this.createdUser, requesterId
        );
    }

    public Category reorder(int newSortKey, Long requesterId) {
        if (requesterId == null) {
            throw new IllegalArgumentException("수정자 id는 필수입니다.");
        }
        permissionCheck(requesterId);
        Instant now = Instant.now();

        return new Category(
                this.id, this.ownerId, this.name, newSortKey,
                this.createdAt, now, this.createdUser, requesterId
        );
    }

    public Category withId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 값을 넣어주세요.");
        }
        return new Category(
                id, this.ownerId, this.name, this.sortKey,
                this.createdAt, this.updatedAt, this.createdUser, this.updatedUser
        );
    }

    public void permissionCheck(Long requesterId) {
        if (!Objects.equals(this.ownerId, requesterId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    public boolean isOwner(Long requesterId) {
        return Objects.equals(this.ownerId, requesterId);
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
