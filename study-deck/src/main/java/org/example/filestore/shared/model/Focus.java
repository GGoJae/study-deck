package org.example.filestore.shared.model;

import java.util.Objects;

public record Focus(
        Long categoryId,
        Long subCategoryId,
        Type targetType,
        Long targetId
) {
    public Focus changeCategoryFocus(Long categoryId) {
        return new Focus(categoryId, null, null, null);
    }

    private Focus reset() {
        return new Focus(null, null, null, null);
    }

    public Focus ifIsCurrentCategoryReset(Long categoryId) {
        if (Objects.equals(this.categoryId, categoryId)) {
            return this.reset();
        }
        return this;
    }

    public static Focus empty() {
        return new Focus(null, null, null, null);
    }

    public Focus ifIsCurrentSubCategoryReset(Long subCategoryId) {
        if (Objects.equals(this.subCategoryId, subCategoryId)) {
            return new Focus(this.categoryId, null, null, null);
        }
        return this;
    }

    public Focus changeSubCategoryFocus(Long subCategoryId) {
        if (Objects.equals(this.subCategoryId, subCategoryId)) {
            return this;
        }

        return new Focus(this.categoryId, subCategoryId, null, null);
    }

    public Focus changeContent(Type type, Long contentId) {
        return new Focus(this.categoryId, this.subCategoryId, type, contentId);
    }
}
