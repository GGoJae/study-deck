package org.example.filestore.shared.model;

import java.util.Objects;

public record MetaDataModel(
    Focus focus,
    Counters counters
) {
    public MetaDataModel changeCategoryFocus(Long categoryId) {
        return new MetaDataModel(this.focus.changeCategoryFocus(categoryId), this.counters);
    }

    public MetaDataModel changeSubCategoryFocus(Long subCategoryId) {
        return new MetaDataModel(this.focus.changeSubCategoryFocus(subCategoryId), this.counters);
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

    public MetaDataModel ifIsCurrentSubCategoryReset(Long subCategoryId) {
        Focus newFocus = this.focus.ifIsCurrentSubCategoryReset(subCategoryId);
        if (Objects.equals(newFocus, this.focus)) {
            return this;
        }

        return new MetaDataModel(newFocus, this.counters);
    }

    public Long nextCategoryId() {
        return this.counters.nextCategoryId();
    }

    public Long nextSubCategoryId() {
        return this.counters.nextSubCategoryId();
    }

    public MetaDataModel increaseNextSubCategoryId() {
        return new MetaDataModel(this.focus, this.counters.increaseSubCategoryId());
    }

    public Long selectedCategoryId() {
        return this.focus.categoryId();
    }

    public Long selectedSubCategoryId() {
        return this.focus.subCategoryId();
    }
}
