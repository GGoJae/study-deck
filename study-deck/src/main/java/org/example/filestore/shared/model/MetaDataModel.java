package org.example.filestore.shared.model;

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
}
