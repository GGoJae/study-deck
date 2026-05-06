package org.example.filestore.shared.model;

import java.util.Objects;

public record MetaDataModel(
    Focus focus,
    Counters counters
) {
    public MetaDataModel changeCategoryFocus(Long categoryId) {
        return new MetaDataModel(this.focus.changeCategoryFocus(categoryId), this.counters);
    }

    public MetaDataModel increaseNextCategoryId() {
        return new MetaDataModel(this.focus, this.counters.increaseCategoryId());
    }

    public MetaDataModel ifIsCurrentCategoryReset(Long categoryId) {
        Focus newFocus = this.focus.ifIsCurrentCategoryReset(categoryId);
        if (Objects.equals(newFocus, focus)) {
            return this;
        }

        return new MetaDataModel(newFocus, this.counters);
    }
}
