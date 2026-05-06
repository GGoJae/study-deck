package org.example.filestore.subcategory.model;

import java.time.Instant;

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
}
