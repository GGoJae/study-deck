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
}
