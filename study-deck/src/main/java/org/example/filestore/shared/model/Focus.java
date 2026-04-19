package org.example.filestore.shared.model;

public record Focus(
        Long categoryId,
        Long subCategoryId,
        Type targetType,
        Long targetId
) {
}
