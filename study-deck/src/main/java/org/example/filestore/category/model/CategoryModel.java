package org.example.filestore.category.model;

import org.example.core.domain.category.Category;

import java.time.Instant;
import java.util.Objects;

public record CategoryModel(
        Long id,
        String fileName,
        Long ownerId,
        String name,
        int sortKey,
        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {

    public CategoryModel update(Category category) {
        if (!Objects.equals(this.id, category.getId())) {
            throw new IllegalStateException("변경하려는 categoryId 가 일치하지 않습니다.");
        }
        return new CategoryModel(
                category.getId(), this.fileName, category.getOwnerId(),
                category.getName(), category.getSortKey(), category.getCreatedAt(),
                category.getUpdatedAt(), category.getCreatedUser(), category.getUpdatedUser()
        );
    }
}
