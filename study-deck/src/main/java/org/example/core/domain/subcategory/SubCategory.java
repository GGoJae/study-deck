package org.example.core.domain.subcategory;

import java.time.Instant;
import java.util.Objects;

public class SubCategory {
    public SubCategory(Long id, Long ownerId, Long categoryId, String name, int sortKey, Instant createdAt, Instant updatedAt, Long createdUser, Long updatedUser) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId는 필수입니다.");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId는 필수입니다.");
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
        this.parentCategoryId = categoryId;
        this.name = name;
        this.sortKey = sortKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdUser = createdUser;
        this.updatedUser = updatedUser;
    }

    private final Long id;
    private final Long ownerId;
    private final Long parentCategoryId;
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

    public Long getParentCategoryId() {
        return parentCategoryId;
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

    public boolean isOwner(Long subCategoryId) {
        return Objects.equals(this.ownerId, subCategoryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCategory that = (SubCategory) o;
        return getSortKey() == that.getSortKey() && Objects.equals(getId(), that.getId()) && Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getParentCategoryId(), that.getParentCategoryId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt()) && Objects.equals(getCreatedUser(), that.getCreatedUser()) && Objects.equals(getUpdatedUser(), that.getUpdatedUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId(), getParentCategoryId(), getName(), getSortKey(), getCreatedAt(), getUpdatedAt(), getCreatedUser(), getUpdatedUser());
    }
}
