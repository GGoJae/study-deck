package org.example.filestore.shared.model;

public record Focus(
        Long categoryId,
        Long subCategoryId,
        Type targetType,
        Long targetId
) {
    public Focus changeCategoryFocus(Long categoryId) {
        return new Focus(categoryId, null, null, null);
    }

    public static Focus empty() {
        return new Focus(null, null, null, null);
    }
}
