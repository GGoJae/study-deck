package org.example.filestore.subcategory.model;

import org.example.core.domain.subcategory.SubCategory;

import java.time.Instant;
import java.util.Objects;

public record SubCategoryModel(
        Long id,
        String filename,
        Long ownerId,
        Long parentCategoryId,
        String name,
        int sortKey,
        Instant createdAt,
        Instant updatedAt,
        Long createdUser,
        Long updatedUser
) {
    public static SubCategoryModel of(SubCategory domain, String filename) {
        return new SubCategoryModel(
                domain.getId(), filename, domain.getOwnerId(),
                domain.getParentCategoryId(), domain.getName(), domain.getSortKey(),
                domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }

    public SubCategoryModel update(SubCategory subCategory) {
        if (!Objects.equals(this.id, subCategory.getId())) {
            throw new IllegalStateException("변경하려는 subCategoryId 가 일치하지 않습니다.");
        }
        return new SubCategoryModel(
                subCategory.getId(), this.filename, subCategory.getOwnerId(),
                subCategory.getParentCategoryId(), subCategory.getName(),
                subCategory.getSortKey(), subCategory.getCreatedAt(),
                subCategory.getUpdatedAt(), subCategory.getCreatedUser(),
                subCategory.getUpdatedUser()
        );
    }
}
